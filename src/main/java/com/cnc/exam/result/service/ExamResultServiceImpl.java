package com.cnc.exam.result.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.service.AbstractBaseServiceImpl;
import com.cnc.exam.result.entity.ExamResultEntity;
import com.cnc.exam.result.repository.ExamResultRepository;

@Transactional
@Service("examResultService")
public class ExamResultServiceImpl extends
		AbstractBaseServiceImpl<ExamResultEntity, Long> implements ExamResultService {

	@Autowired
	private ExamResultRepository examResultRepository;

	private Specification<ExamResultEntity> buildSpecification(
			final Map<String, Object> searchParams) {

		Specification<ExamResultEntity> spec = new Specification<ExamResultEntity>() {
			@Override
			public Predicate toPredicate(Root<ExamResultEntity> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Predicate allCondition = null;
				String examId = (String) searchParams.get("examId");
				String userId = (String) searchParams.get("userId");
				String isPass = (String) searchParams.get("isPass");
				String deptId = (String) searchParams.get("deptId");
				String createTimeRange=(String) searchParams.get("createTime");
				if (examId != null && !"".equals(examId)) {
					Predicate condition = cb.like(root.get("exam").get("id").as(String.class), "%"+ searchParams.get("examId") + "%");
					if (null == allCondition)
						allCondition = cb.and(condition);
					else
						allCondition = cb.and(allCondition, condition);
				}
				if (userId != null && !"".equals(userId)) {
					Predicate condition = cb.like(root.get("user").get("id").as(String.class), "%"+ searchParams.get("userId") + "%");
					if (null == allCondition)
						allCondition = cb.and(condition);
					else
						allCondition = cb.and(allCondition, condition);

				}
				if (isPass != null && !"".equals(isPass)) {
					Predicate condition = cb.like(root.get("isPass").as(String.class), "%"+ searchParams.get("isPass") + "%");
					if (null == allCondition)
						allCondition = cb.and(condition);
					else
						allCondition = cb.and(allCondition, condition);
				}
				if (deptId != null && !"".equals(deptId)) {
					Predicate condition = cb.like(root.get("user").get("department").get("id").as(String.class), "%"+ searchParams.get("deptId") + "%");
					if (null == allCondition)
						allCondition = cb.and(condition);
					else
						allCondition = cb.and(allCondition, condition);
				}
				
				if(createTimeRange!=null&&!"".equals(createTimeRange)){			
					String createTimeStartStr=createTimeRange.split(" - ")[0]+":00:00:00";
					String createTimeEndStr=createTimeRange.split(" - ")[1]+":23:59:59";
					SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy:hh:mm:ss");
					System.out.println(createTimeStartStr);
					try {
						Date createTimeStart = format.parse(createTimeStartStr);
						Date createTimeEnd=format.parse(createTimeEndStr);
						Predicate condition=cb.between(root.get("createTime").as(Date.class),createTimeStart, createTimeEnd);
						if(null==allCondition)
							allCondition=cb.and(condition);
						else
							allCondition=cb.and(allCondition,condition);
					} catch (ParseException e) {
						e.printStackTrace();
						Logger log =LoggerFactory.getLogger(this.getClass());
					}
				}	
				return allCondition;
			}
		};
		return spec;
	}

	@Override
	public Page<ExamResultEntity> findERByCondition(
			Map<String, Object> searchParams, Pageable pageable) {
		return examResultRepository.findAll(buildSpecification(searchParams),
				pageable);
	}
	
	@Override
	public void update(ExamResultEntity obj){
		ExamResultEntity obj2=examResultRepository.findOne(obj.getId());
		try {
			@SuppressWarnings("unchecked")
			Class<ExamResultEntity> clazz=(Class<ExamResultEntity>) Class.forName("com.cnc.exam.result.entity.ExamResultEntity");
			Method[] methods=clazz.getDeclaredMethods();
			for(Method m:methods){
				 if(m.getName().substring(0, 3).equals("get")){					
					Object value=m.invoke(obj);
					if(value!=null){
					Method setMethod=clazz.getDeclaredMethod("set"+m.getName().substring(3, 4).toUpperCase()+m.getName().substring(4),m.getReturnType());
					setMethod.invoke(obj2, value);
					}
				}		
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void saveToExcel(String path) throws FileNotFoundException{
		List<ExamResultEntity> eres=examResultRepository.findAll();
		File file=new File(path);
		createExcel(new FileOutputStream(file), eres);
	}
	
	private void createExcel(OutputStream os,List<ExamResultEntity> list){
		String[] heads={"用户名","课程名称","考试成绩","通过情况"};
		// 创建工作区
		WritableWorkbook workbook=null;
		try {
			workbook = Workbook.createWorkbook(os);
		// 创建新的一页，sheet只能在工作簿中使用
		WritableSheet sheet = workbook.createSheet("user sheet1", 0);
		// 创建单元格即具体要显示的内容，new Label(0,0,"用户") 第一个参数是column 第二个参数是row
		// 第三个参数是content，第四个参数是可选项,为Label添加字体样式
		// 通过sheet的addCell方法添加Label，注意一个cell/label只能使用一次addCell
		// 第一个参数为列，第二个为行，第三个文本内容
		//添加字段名
		for(int i=0;i<heads.length;i++){
			sheet.addCell(new Label(i,0,heads[i]));
		}
		//添加字段内容
		for(int i=0;i<list.size();i++){
			sheet.addCell(new Label(0, i+1, list.get(i).getUser().getUsername()));
			sheet.addCell(new Label(1, i+1, list.get(i).getExam().getCourseId()+""));
			sheet.addCell(new Label(2, i+1, list.get(i).getCreateTime().toString()));
			sheet.addCell(new Label(2, i+1, list.get(i).getScore()+""));
			switch(list.get(i).getIsPass()){
				case 0:{
					sheet.addCell(new Label(3, i+1, "通过"));
					break;
				}
				case 1:{
					sheet.addCell(new Label(3, i+1, "不通过"));
					break;
				}
				case 2:{
					sheet.addCell(new Label(3, i+1, "请假"));
					break;
				}
			}
		}
		workbook.write();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			// 将内容写到输出流中，然后关闭工作区，最后关闭输出流
			
			try {
				if(workbook!=null)
				workbook.close();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(os!=null)
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
