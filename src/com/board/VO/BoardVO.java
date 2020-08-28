package com.board.VO;

import java.sql.Date;

public class BoardVO
{
	private int no;
  public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

private int rnum;
  private int seq;
  private String name;
  private String title;
  private String content;
  private String pass;
  private int hit;
  private Date regdate;
  private int ref;
  private int indent;
  private int step;
  
  private BoardFileVO BoardFileVO;
  
  public int getRef() {
	return ref;
}

public void setRef(int ref) {
	this.ref = ref;
}

public int getIndent() {
	return indent;
}

public void setIndent(int indent) {
	this.indent = indent;
}

public int getStep() {
	return step;
}

public void setStep(int step) {
	this.step = step;
}

public int getSeq()
  {
    return this.seq;
  }
  
  public void setSeq(int seq)
  {
    this.seq = seq;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getTitle()
  {
    return this.title;
  }
  
  public void setTitle(String title)
  {
    this.title = title;
  }
  
  public String getContent()
  {
    return this.content;
  }
  
  public void setContent(String content)
  {
    this.content = content;
  }
  
  public String getPass()
  {
    return this.pass;
  }
  
  public void setPass(String pass)
  {
    this.pass = pass;
  }
  
  public int getHit()
  {
    return this.hit;
  }
  
  public void setHit(int hit)
  {
    this.hit = hit;
  }
  
  public Date getRegdate()
  {
    return this.regdate;
  }
  
  public void setRegdate(Date regdate)
  {
    this.regdate = regdate;
  }



public int getRnum() {
	return rnum;
}

public void setRnum(int rnum) {
	this.rnum = rnum;
}

public BoardFileVO getBoardFileVO() {
	return BoardFileVO;
}

public void setBoardFileVO(BoardFileVO boardFileVO) {
	BoardFileVO = boardFileVO;
}
  
  
  
}
