package com.board.dao;

import com.board.VO.BoardVO;
import com.board.VO.BoardFileVO;
import com.board.dao.BoardDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

@Component
public class BoardDaoImpl extends SqlSessionDaoSupport implements BoardDao {
	
	public List<BoardVO> list(Map<String, Object> map) {
		System.out.println("dao="+map.get("startnum"));
		System.out.println("dao="+map.get("sc"));
		List<BoardVO> list = getSqlSession().selectList("boardList", map);
		return list;
	}

	public int getCount(Map<String, Object> map) {
		return ((Integer) getSqlSession().selectOne("boardCount", map)).intValue();
	}

	@Override
	public void add(BoardVO boardVO) {
		getSqlSession().insert("write",boardVO);	
	}

	@Override
	public BoardVO detail(int seq) {
		BoardVO bean=getSqlSession().selectOne("detail",seq);
		return bean;
	}

	@Override
	public void update(BoardVO boardVO) {
		getSqlSession().update("update",boardVO);	
		
	}

	@Override
	public void delete(int seq) {
		getSqlSession().delete("delete",seq);
		
	}

	@Override
	public int cnt(int seq) {
		return ((int) getSqlSession().update("cnt", seq));
	}

	@Override
	public List<BoardVO> searchlist(HashMap<String, Object> map) {
		List<BoardVO> list = getSqlSession().selectList("searchboardList", map);
		return list;
	}

	@Override
	public void reply(BoardVO boardVO) {
		getSqlSession().insert("reply",boardVO);
		
	}

	@Override
	public void updaterep(BoardVO boardVO) {
		getSqlSession().update("updaterep",boardVO);	
		
	}

	@Override
	public void updatestep(BoardVO boardVO) {
		getSqlSession().update("updatestep",boardVO);	
		
	}

	@Override
	public List<BoardVO> filesearchlist(HashMap<String, Object> map) {
		List<BoardVO> list = getSqlSession().selectList("filesearchboardList", map);
		return list;
	}
	

}
