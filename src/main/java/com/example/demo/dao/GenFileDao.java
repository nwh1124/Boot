package com.example.demo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.dto.GenFile;

@Mapper
public interface GenFileDao {
	
	void saveMeta(Map<String, Object> param);
	
	GenFile getGenFile(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId, 
			@Param("typeCode") String typeCode, @Param("type2Code") String type2Code, 
			@Param("fileNo") int fileNo);
	
	List<GenFile> getGenFiles(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId);
	
	void changeRelId(@Param("id") int id, @Param("relId") int relId);

	void deleteFiles(@Param("relTypeCode") String relTypeCode, @Param("relId") int relId);
	
	void deleteFile(@Param("id")int id);

}