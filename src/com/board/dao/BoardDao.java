package com.board.dao;

import com.board.VO.BoardVO;
import com.board.VO.BoardFileVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract interface BoardDao {
	
	public abstract List<BoardVO> list(Map<String, Object> map);

	public abstract int getCount(Map<String, Object> paramMap);

	void add(BoardVO boardVO);

	public abstract BoardVO detail(int seq);

	public abstract void update(BoardVO boardVO);

	public abstract void delete(int seq);

	public abstract int cnt(int seq);

	public abstract List<BoardVO> searchlist(HashMap<String, Object> map);

	public abstract void reply(BoardVO boardVO);

	public abstract void updaterep(BoardVO boardVO);

	public abstract void updatestep(BoardVO boardVO);

	public abstract List<BoardVO> filesearchlist(HashMap<String, Object> map);

	


}
