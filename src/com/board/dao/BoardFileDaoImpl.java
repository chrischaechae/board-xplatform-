package com.board.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import com.board.VO.BoardFileVO;
import com.board.VO.BoardVO;

@Component
public class BoardFileDaoImpl extends SqlSessionDaoSupport implements BoardFileDao{

	@Override
	public void addfile(HashMap<String, Object> map) {
		
		getSqlSession().insert("addfile",map);	
	}

	@Override
	public List<BoardFileVO> list(HashMap<String, Object> map,int gseq) {
		List<BoardFileVO> list = getSqlSession().selectList("boardflieList",gseq);
		return list;
	}

	@Override
	public void delete(int seq) {
		getSqlSession().delete("delfile",seq);	
		
	}

	@Override
	public void updatefile(HashMap<String, Object> map) {
		getSqlSession().insert("updatefile",map);
		
	}

	@Override
	public void deleterefile(String delfile) {
		getSqlSession().delete("delrefile",delfile);	
		
	}

	
}
