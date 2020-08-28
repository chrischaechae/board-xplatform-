package com.board.controller;


import com.board.VO.BoardVO;
import com.board.VO.BoardFileVO;
import com.board.dao.BoardDao;
import com.board.dao.BoardFileDao;
import com.board.paging.Paging;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.tobesoft.xplatform.data.*;
import com.tobesoft.xplatform.tx.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.net.URLDecoder;
import java.io.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.StyledEditorKit.BoldAction;

import org.apache.log4j.Logger;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ListController {
	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(getClass());
	private int pageSize = 10;
	private int blockCount = 10;
		
	
	@Autowired
	private BoardDao boardDao;
	@Autowired
	private BoardFileDao boardfildDao;
	
	
	@RequestMapping({ "/board/list.do" })
	public void list(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="pagenum",defaultValue = "1",required=false) int pagenum,@RequestParam(value="startnum",defaultValue = "1",required=false) int startnum,@RequestParam(value="contentperpage",defaultValue = "4",required=false) int contentperpage) throws SQLException, Exception {
			
		System.out.println(pagenum);
		System.out.println(startnum);
		System.out.println(contentperpage);
		///////////////////
		PlatformData pdata = new PlatformData();
		HttpPlatformRequest req = new HttpPlatformRequest(request); 
			req.receiveData();							
			pdata = req.getData();
		

		int nErrorCode = 0;
		String strErrorMsg = "START";
		
		/*int pageNum = new Integer(request.getParameter("pageNum"));
		int startNum = new Integer(request.getParameter("startNum"));
		int contentPerPage = new Integer(request.getParameter("contentPerPage"));*/
				
		/*DataSet ds1 = pdata.getDataSet("page");
		
		pagenum =Integer.parseInt(ds1.getString(0,"pagenum"));
		startnum =Integer.parseInt(ds1.getString(0,"startnum"));
		contentperpage =Integer.parseInt(ds1.getString(0,"contentperpage"));*/
		
		int sc=startnum+contentperpage;
		System.out.println(pagenum);
		
					// DataSet
					DataSet ds = new DataSet("board");			
				    
				    // DataSet Column setting
					ds.addColumn("no", DataTypes.INT, 256);
					ds.addColumn("rnum", DataTypes.INT, 256);
   				    ds.addColumn("seq", DataTypes.INT, 256);
				    ds.addColumn("title", DataTypes.STRING,  256);
				    ds.addColumn("name", DataTypes.STRING,  256);
					ds.addColumn("regdate", DataTypes.DATE, 256);
					ds.addColumn("hit", DataTypes.INT, 256);
					ds.addColumn("pass", DataTypes.STRING, 256);
					ds.addColumn("indent", DataTypes.INT, 256);
					ds.addColumn("step", DataTypes.INT, 256);
					ds.addColumn("ref", DataTypes.INT, 256);
					
					// DAO
					BoardVO boardVO;
					HashMap<String, Object> map = new HashMap();
					map.put("startnum",startnum);
					map.put("sc", sc);
					
				   // strErrorMsg  = 0;
					List<BoardVO> list = null;
					list=this.boardDao.list(map);
				   
				   
				   // ResultSet -> Show the Row sets (XML) : browser 
				   for (int i=0; i<list.size(); i++) {
					   int row = ds.newRow();
					   
					   	boardVO = new BoardVO();
					   	boardVO = list.get(i);
					    
					   	ds.set(row, "no", boardVO.getNo());
					   	ds.set(row, "rnum", boardVO.getRnum());
					   	ds.set(row, "seq", boardVO.getSeq());
					    ds.set(row, "title", boardVO.getTitle());
					    ds.set(row, "name", boardVO.getName());
					    ds.set(row, "regdate",boardVO.getRegdate());
					    ds.set(row, "hit", boardVO.getHit());
					    ds.set(row, "pass", boardVO.getPass());
					    ds.set(row, "indent", boardVO.getIndent());
					    ds.set(row, "step", boardVO.getStep());
					    ds.set(row, "ref", boardVO.getRef());
					   
				   }
					 // for
					pdata.addDataSet(ds);
					
			       // set the ErrorCode and ErrorMsg about success
			       nErrorCode = 0;
			       strErrorMsg = "SUCC";
				    
		
		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
				
		varList.add("ErrorCode", nErrorCode);
		varList.add("ErrorMsg", strErrorMsg);
				
		// send the result data(XML) to Client
		HttpPlatformResponse res 
		    = new HttpPlatformResponse(response, 
			       									            PlatformType.CONTENT_TYPE_XML,  
			       									            "UTF-8");
		res.setData(pdata); 
		res.sendData();		// Send Data
	
		
		///////////////////
	}
	@RequestMapping(value="/board/write.do",method=RequestMethod.POST)
	public void write(HttpServletRequest request, HttpServletResponse response) throws PlatformException{
		PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";
		
		
					// receive client request
					// not need to receive
				    
					// create HttpPlatformRequest for receive data from client
					HttpPlatformRequest req 
					     = new HttpPlatformRequest(request); 

					req.receiveData();							
					pdata = req.getData();
					
					DataSet ds = pdata.getDataSet("write");
					
					// DAO
					BoardVO boardVO;
					boardVO = new BoardVO();
					
					boardVO.setTitle(ds.getString(0, "title"));
					boardVO.setName(ds.getString(0, "nname"));
					boardVO.setContent(ds.getString(0, "content")); 
					boardVO.setPass(ds.getString(0, "pass"));
					
					
					boardDao.add(boardVO);
					
				    // set the ErrorCode and ErrorMsg about success
				    nErrorCode = 0;
				    strErrorMsg = "SUCC";
				    
			
		
		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
				
		varList.add("ErrorCode", nErrorCode);
		varList.add("ErrorMsg", strErrorMsg);
				
		// send the result data(XML) to Client
		HttpPlatformResponse res 
		    = new HttpPlatformResponse(response, 
			       									            PlatformType.CONTENT_TYPE_XML,  
			       									            "UTF-8");
		res.setData(pdata); 
		res.sendData();		// Send Data
	
	}
	@RequestMapping({ "/board/detail.do" })
	public void detail(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="seq",required=false) int seq) throws PlatformException{
		
		PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";
		
		DataSet ds = new DataSet("detail");			
	    
	    // DataSet Column setting
	    ds.addColumn("title", DataTypes.STRING,  256);
	    ds.addColumn("name", DataTypes.STRING,  256);
	    ds.addColumn("content", DataTypes.STRING, 256);
	    ds.addColumn("pass", DataTypes.STRING, 256);
	    ds.addColumn("indent", DataTypes.INT, 256);
	    ds.addColumn("step", DataTypes.INT, 256);
		
		BoardVO bean=null;
		
		bean=boardDao.detail(seq);
		boardDao.cnt(seq);
		
		int row = ds.newRow();
		
		
	    ds.set(row, "title", bean.getTitle());
	    ds.set(row, "name", bean.getName());
	    ds.set(row, "content",bean.getContent());
	    ds.set(row, "pass",bean.getPass());
	    ds.set(row, "indent",bean.getIndent());
	    ds.set(row, "step",bean.getStep());
	    pdata.addDataSet(ds);
		
	       // set the ErrorCode and ErrorMsg about success
	       nErrorCode = 0;
	       strErrorMsg = "SUCC";
		    

		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
				
		varList.add("ErrorCode", nErrorCode);
		varList.add("ErrorMsg", strErrorMsg);
				
		// send the result data(XML) to Client
		HttpPlatformResponse res 
		 = new HttpPlatformResponse(response, 
			       									            PlatformType.CONTENT_TYPE_XML,  
			       									            "UTF-8");
		res.setData(pdata); 
		res.sendData();		// Send Data
	}
	@RequestMapping({ "/board/detailfile.do" })
	public void detailfile(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="seq",required=false) int gseq) throws PlatformException{
		
		PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";
		
		// DataSet
		DataSet ds = new DataSet("file1");			
	    
	    // DataSet Column setting
		ds.addColumn("no", DataTypes.INT, 256);
		ds.addColumn("upload", DataTypes.STRING,  256);
		ds.addColumn("path", DataTypes.STRING,  256);
		
		// DAO
		BoardFileVO boardfileVO;
		HashMap<String, Object> map = new HashMap();
	   // strErrorMsg  = 0;
		List<BoardFileVO> list = null;
		
		
		list=this.boardfildDao.list(map,gseq);
		
	   
	   // ResultSet -> Show the Row sets (XML) : browser 
	   for (int i=0; i<list.size(); i++) {
		   int row = ds.newRow();
		   
		   boardfileVO = new BoardFileVO();
		   boardfileVO = list.get(i);
		    
		   	ds.set(row, "no", boardfileVO.getNo());
		    ds.set(row, "upload", boardfileVO.getOriupload());
		    ds.set(row, "path", boardfileVO.getPath());

	   }
		 // for
		pdata.addDataSet(ds);
		
       // set the ErrorCode and ErrorMsg about success
       nErrorCode = 0;
       strErrorMsg = "SUCC";
	    

	// save the ErrorCode and ErrorMsg for sending Client
	VariableList varList = pdata.getVariableList();
		
	varList.add("ErrorCode", nErrorCode);
	varList.add("ErrorMsg", strErrorMsg);
		
	// send the result data(XML) to Client
	HttpPlatformResponse res 
	= new HttpPlatformResponse(response, 
	       									            PlatformType.CONTENT_TYPE_XML,  
	       									            "UTF-8");
	res.setData(pdata); 
	res.sendData();		// Send Data

	}
	
	
	@RequestMapping(value="/board/edit.do")
	public void edit(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="seq",required=false) int seq) throws IOException, PlatformException{
		
		PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";
		
		DataSet ds = new DataSet("edit");			
	    
	    // DataSet Column setting
	    ds.addColumn("title", DataTypes.STRING,  256);
	    ds.addColumn("name", DataTypes.STRING,  256);
	    ds.addColumn("content", DataTypes.STRING, 256);
	    ds.addColumn("pass", DataTypes.STRING, 256);
		
		BoardVO bean=null;
		
		bean=boardDao.detail(seq);
		
		int row = ds.newRow();
		
		
	    ds.set(row, "title", bean.getTitle());
	    ds.set(row, "name", bean.getName());
	    ds.set(row, "content",bean.getContent());
	    ds.set(row, "pass",bean.getPass());
		
	    pdata.addDataSet(ds);
		
	       // set the ErrorCode and ErrorMsg about success
	       nErrorCode = 0;
	       strErrorMsg = "SUCC";
		    

		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
				
		varList.add("ErrorCode", nErrorCode);
		varList.add("ErrorMsg", strErrorMsg);
				
		// send the result data(XML) to Client
		HttpPlatformResponse res 
		 = new HttpPlatformResponse(response, 
			       									            PlatformType.CONTENT_TYPE_XML,  
			       									            "UTF-8");
		res.setData(pdata); 
		res.sendData();		// Send Data
		}
	@RequestMapping({ "/board/editfile.do" })
	public void editfile(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="seq",required=false) int gseq) throws PlatformException{
		
		PlatformData pdata = new PlatformData();

		int nErrorCode = 0;
		String strErrorMsg = "START";
		
		// DataSet
		DataSet ds = new DataSet("file2");			
	    
	    // DataSet Column setting
		ds.addColumn("fseq", DataTypes.INT, 256);
		ds.addColumn("upload", DataTypes.STRING,  256);
		ds.addColumn("path", DataTypes.STRING,  256);
		
		// DAO
		BoardFileVO boardfileVO;
		HashMap<String, Object> map = new HashMap();
	   // strErrorMsg  = 0;
		List<BoardFileVO> list = null;
		
		
		list=this.boardfildDao.list(map,gseq);
		
	   
	   // ResultSet -> Show the Row sets (XML) : browser 
	   for (int i=0; i<list.size(); i++) {
		   int row = ds.newRow();
		   
		   boardfileVO = new BoardFileVO();
		   boardfileVO = list.get(i);
		    
		   	ds.set(row, "fseq", boardfileVO.getFseq());
		    ds.set(row, "upload", boardfileVO.getOriupload());
		    ds.set(row, "path", boardfileVO.getPath());

	   }
		 // for
		pdata.addDataSet(ds);
		
       // set the ErrorCode and ErrorMsg about success
       nErrorCode = 0;
       strErrorMsg = "SUCC";
	    

		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
			
		varList.add("ErrorCode", nErrorCode);
		varList.add("ErrorMsg", strErrorMsg);
			
		// send the result data(XML) to Client
		HttpPlatformResponse res 
		= new HttpPlatformResponse(response, 
		       									            PlatformType.CONTENT_TYPE_XML,  
		       									            "UTF-8");
		res.setData(pdata); 
		res.sendData();		// Send Data

	}
	
		@RequestMapping(value="/board/update.do")
		public void update(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="seq",required=false) int seq) throws PlatformException{
			
			PlatformData pdata = new PlatformData();

			int nErrorCode = 0;
			String strErrorMsg = "START";
			
			
						// receive client request
						// not need to receive
					    
						// create HttpPlatformRequest for receive data from client
						HttpPlatformRequest req 
						     = new HttpPlatformRequest(request); 

						req.receiveData();							
						pdata = req.getData();
						
						DataSet ds = pdata.getDataSet("edit");
						
						// DAO
						BoardVO boardVO;
						boardVO = new BoardVO();
						
						boardVO.setSeq(seq);
						boardVO.setTitle(ds.getString(0, "title"));
						boardVO.setName(ds.getString(0, "name"));
						boardVO.setContent(ds.getString(0, "content")); 
						boardVO.setPass(ds.getString(0, "pass"));
						
						boardDao.update(boardVO);
						
					    // set the ErrorCode and ErrorMsg about success
					    nErrorCode = 0;
					    strErrorMsg = "SUCC";
					    
				
			
			// save the ErrorCode and ErrorMsg for sending Client
			VariableList varList = pdata.getVariableList();
					
			varList.add("ErrorCode", nErrorCode);
			varList.add("ErrorMsg", strErrorMsg);
					
			// send the result data(XML) to Client
			HttpPlatformResponse res 
			    = new HttpPlatformResponse(response, 
				       									            PlatformType.CONTENT_TYPE_XML,  
				       									            "UTF-8");
			res.setData(pdata); 
			res.sendData();		// Send Data
		}
		@RequestMapping(value="/board/delete.do")
		public void delete(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="seq",required=false) int seq) throws PlatformException{
			
			PlatformData pdata = new PlatformData();

			int nErrorCode = 0;
			String strErrorMsg = "START";
			
			
						// receive client request
						// not need to receive
					    
						// create HttpPlatformRequest for receive data from client
						HttpPlatformRequest req 
						     = new HttpPlatformRequest(request); 

						req.receiveData();							
						pdata = req.getData();
						
						boardDao.delete(seq);
						boardfildDao.delete(seq);
						
						
					    // set the ErrorCode and ErrorMsg about success
					    nErrorCode = 0;
					    strErrorMsg = "SUCC";
					    
				
			
			// save the ErrorCode and ErrorMsg for sending Client
			VariableList varList = pdata.getVariableList();
					
			varList.add("ErrorCode", nErrorCode);
			varList.add("ErrorMsg", strErrorMsg);
					
			// send the result data(XML) to Client
			HttpPlatformResponse res 
			    = new HttpPlatformResponse(response, 
				       									            PlatformType.CONTENT_TYPE_XML,  
				       									            "UTF-8");
			res.setData(pdata); 
			res.sendData();		// Send Data
		}
		@RequestMapping(value="/board/upload.do",method=RequestMethod.POST)
		public void upload(HttpServletRequest request, HttpServletResponse response) throws PlatformException, IOException{
			
			String sHeader = request.getHeader("Content-Type");
			if (sHeader == null)
			{
				return;
			}
			request.setCharacterEncoding("utf-8");
			String sRealPath = request.getSession().getServletContext().getRealPath("/");
			
			String sPath     = request.getParameter("PATH");
			String sUpPath   = sRealPath + sPath;
			int    nMaxSize  = 500 * 1024 * 1024; // �ִ� ���ε� ���� ũ�� 500MB(�ް�)�� ����

			PlatformData resData    = new PlatformData();
			VariableList resVarList = resData.getVariableList();
			
			String sMsg = " A ";
			try 
			{
				MultipartRequest multi = new MultipartRequest(request, sUpPath, nMaxSize, "utf-8", new DefaultFileRenamePolicy());
				Enumeration files      = multi.getFileNames();
				
				
				sMsg += "B ";
				DataSet ds = new DataSet("Dataset00");		
				ds.addColumn(new ColumnHeader("FILENAME", DataTypes.STRING));
				ds.addColumn(new ColumnHeader("FILETYPE", DataTypes.STRING));
				ds.addColumn(new ColumnHeader("FILESIZE", DataTypes.LONG));
				
				sMsg += "C ";
				String sFName = "";
				String sName  = "";
				String stype  = "";
				String orisFName = "";
				File   f      = null;
				
				while (files.hasMoreElements()) 
				{
					sMsg += "D ";
					sName  = (String)files.nextElement();
					sFName = multi.getFilesystemName(sName);
					orisFName=multi.getOriginalFileName(sName);
					stype  = multi.getContentType(sName);
					
					
					int nRow = ds.newRow();
					ds.set(nRow, "FILENAME", sFName);
					ds.set(nRow, "FILETYPE", stype);
					
					f = multi.getFile(sName);			
					if (f != null)
					{
						ds.set(nRow, "FILESIZE", f.length());
					}		
					sMsg += nRow +" ";
				}
				String gdpath=sUpPath+"\\"+orisFName;
				HashMap<String, Object> map = new HashMap();
				map.put("upload",sFName);
				map.put("oriupload",orisFName);
				map.put("path", gdpath);
				boardfildDao.addfile(map);
				resData.addDataSet(ds);
				resVarList.add("ErrorCode", 200);
				resVarList.add("ErrorMsg", sUpPath+"/"+sFName);
			} 
			catch (Exception e) 
			{
				resVarList.add("ErrorCode", 500);
				resVarList.add("ErrorMsg", sMsg + " " + e);
			}

			HttpPlatformResponse res = new HttpPlatformResponse(response);
			res.setData(resData);
			res.sendData();
		}
		
		@RequestMapping(value="/board/reupload.do",method=RequestMethod.POST)
		public void reupload(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="seq",required=false) int gseq) throws PlatformException, IOException{
			System.out.println(gseq);
			System.out.println("reupload");
			String sHeader = request.getHeader("Content-Type");
			if (sHeader == null)
			{
				return;
			}
			request.setCharacterEncoding("utf-8");
			String sRealPath = request.getSession().getServletContext().getRealPath("/");
			
			String sPath     = request.getParameter("PATH");
			String sUpPath   = sRealPath + sPath;
			int    nMaxSize  = 500 * 1024 * 1024; // �ִ� ���ε� ���� ũ�� 500MB(�ް�)�� ����

			PlatformData resData    = new PlatformData();
			VariableList resVarList = resData.getVariableList();
			
			String sMsg = " A ";
			try 
			{
				MultipartRequest multi = new MultipartRequest(request, sUpPath, nMaxSize, "utf-8", new DefaultFileRenamePolicy());
				Enumeration files      = multi.getFileNames();
				
				
				sMsg += "B ";
				DataSet ds = new DataSet("Dataset01");		
				ds.addColumn(new ColumnHeader("FILENAME", DataTypes.STRING));
				ds.addColumn(new ColumnHeader("FILETYPE", DataTypes.STRING));
				ds.addColumn(new ColumnHeader("FILESIZE", DataTypes.LONG));
				
				sMsg += "C ";
				String sFName = "";
				String sName  = "";
				String stype  = "";
				String orisFName = "";
				File   f      = null;
				
				while (files.hasMoreElements()) 
				{
					sMsg += "D ";
					sName  = (String)files.nextElement();
					sFName = multi.getFilesystemName(sName);
					orisFName=multi.getOriginalFileName(sName);
					stype  = multi.getContentType(sName);
					
					
					int nRow = ds.newRow();
					ds.set(nRow, "FILENAME", sFName);
					ds.set(nRow, "FILETYPE", stype);
					
					f = multi.getFile(sName);			
					if (f != null)
					{
						ds.set(nRow, "FILESIZE", f.length());
					}		
					sMsg += nRow +" ";
				}
				String gdpath=sUpPath+"\\"+orisFName;
				HashMap<String, Object> map = new HashMap();
				map.put("fseq",gseq);
				map.put("upload",sFName);
				map.put("oriupload",orisFName);
				map.put("path", gdpath);
				boardfildDao.updatefile(map);
				resData.addDataSet(ds);
				resVarList.add("ErrorCode", 200);
				resVarList.add("ErrorMsg", sUpPath+"/"+sFName);
			} 
			catch (Exception e) 
			{
				resVarList.add("ErrorCode", 500);
				resVarList.add("ErrorMsg", sMsg + " " + e);
			}

			HttpPlatformResponse res = new HttpPlatformResponse(response);
			res.setData(resData);
			res.sendData();
		}
		@RequestMapping(value="/board/redelfile.do",method=RequestMethod.POST)
		public void redelfile(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="redelfile",required=false) String gepath) throws PlatformException, IOException{
			
			System.out.println("redelfileconn");
			gepath=new String(gepath.getBytes("iso-8859-1"), "utf-8");

			System.out.println(gepath);
			File delfile=new File(gepath);

			if(delfile.exists()){
				delfile.delete();
			}else{
				System.out.println("파일이 없습니다.");
			}
			String[] splitgepath=gepath.split("/");
			System.out.println(splitgepath[9]);
			
			boardfildDao.deleterefile(splitgepath[9]);
			
		}
		@RequestMapping(value="/board/delfile.do",method=RequestMethod.POST)
		public void delfile(HttpServletRequest request, HttpServletResponse response) throws PlatformException, IOException{
				
			PlatformData pdata = new PlatformData();

			int nErrorCode = 0;
			String strErrorMsg = "START";
			
			
						// receive client request
						// not need to receive
					    
						// create HttpPlatformRequest for receive data from client
						HttpPlatformRequest req 
						     = new HttpPlatformRequest(request); 

						req.receiveData();							
						pdata = req.getData();
						
						DataSet ds = pdata.getDataSet("file1");
						
						// DAO
						BoardFileVO BoardFileVO;
						BoardFileVO = new BoardFileVO();
						
						Boolean a=true;
						int i=0;
						while(a){
						BoardFileVO.setUpload(ds.getString(i, "upload"));
		
						String folder="C:\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\spring\\upload\\";
						String file=folder+BoardFileVO.getUpload();
						File delfile=new File(file);
						
							if(delfile.exists()){
								delfile.delete();
							}else{
								break;
							}
							i++;
							
						}//while end
					    // set the ErrorCode and ErrorMsg about success
					    nErrorCode = 0;
					    strErrorMsg = "SUCC";
					    
				
			
			// save the ErrorCode and ErrorMsg for sending Client
			VariableList varList = pdata.getVariableList();
					
			varList.add("ErrorCode", nErrorCode);
			varList.add("ErrorMsg", strErrorMsg);
					
			// send the result data(XML) to Client
			HttpPlatformResponse res 
			    = new HttpPlatformResponse(response, 
				       									            PlatformType.CONTENT_TYPE_XML,  
				       									            "UTF-8");
			res.setData(pdata); 
			res.sendData();		// Send Data	
		    
		   
		}
		@RequestMapping({ "/board/search.do" })
		public void search(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="keyfield",required=false) String keyfield,@RequestParam(value="keyword",required=false) String keyword) throws PlatformException, IOException{
			
			PlatformData pdata = new PlatformData();

			int nErrorCode = 0;
			String strErrorMsg = "START";
					
					
						// DataSet
						DataSet ds = new DataSet("board");			
					    
					    // DataSet Column setting
						ds.addColumn("no", DataTypes.INT, 256);
	   				    ds.addColumn("seq", DataTypes.INT, 256);
					    ds.addColumn("title", DataTypes.STRING,  256);
					    ds.addColumn("name", DataTypes.STRING,  256);
						ds.addColumn("regdate", DataTypes.DATE, 256);
						ds.addColumn("hit", DataTypes.INT, 256);
						ds.addColumn("pass", DataTypes.STRING, 256);
			
			
			
			
			keyword=new String(keyword.getBytes("iso-8859-1"), "utf-8");
			keyfield=new String(keyfield.getBytes("iso-8859-1"), "utf-8");
			System.out.println(keyword);
			System.out.println(keyfield);
			
			
					BoardVO boardVO;
					HashMap<String, Object> map = new HashMap();
					map.put("keyword",keyword);
					map.put("keyfield", keyfield);
					
				   // strErrorMsg  = 0;
					List<BoardVO> list = null;
					list=this.boardDao.searchlist(map);
				   
				   
				   // ResultSet -> Show the Row sets (XML) : browser 
				   for (int i=0; i<list.size(); i++) {
					   int row = ds.newRow();
					   
					   	boardVO = new BoardVO();
					   	boardVO = list.get(i);
					    
					   	ds.set(row, "no", boardVO.getRnum());
					   	ds.set(row, "seq", boardVO.getSeq());
					    ds.set(row, "title", boardVO.getTitle());
					    ds.set(row, "name", boardVO.getName());
					    ds.set(row, "regdate",boardVO.getRegdate());
					    ds.set(row, "hit", boardVO.getHit());
					    ds.set(row, "pass", boardVO.getPass());
					   
				   }
					 // for
					pdata.addDataSet(ds);
					
			       // set the ErrorCode and ErrorMsg about success
			       nErrorCode = 0;
			       strErrorMsg = "SUCC";
				    
		
		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
				
		varList.add("ErrorCode", nErrorCode);
		varList.add("ErrorMsg", strErrorMsg);
				
		// send the result data(XML) to Client
		HttpPlatformResponse res 
		    = new HttpPlatformResponse(response, 
			       									            PlatformType.CONTENT_TYPE_XML,  
			       									            "UTF-8");
		res.setData(pdata); 
		res.sendData();		// Send Data
					
				}
		@RequestMapping({ "/board/filesearch.do" })
		public void filesearch(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="keyfield",required=false) String keyfield,@RequestParam(value="keyword",required=false) String keyword) throws PlatformException, IOException{
			
			PlatformData pdata = new PlatformData();

			int nErrorCode = 0;
			String strErrorMsg = "START";
					
					
						// DataSet
						DataSet ds = new DataSet("board");			
					    
					    // DataSet Column setting
						ds.addColumn("rnum", DataTypes.INT, 256);
						ds.addColumn("no", DataTypes.INT, 256);
	   				    ds.addColumn("seq", DataTypes.INT, 256);
					    ds.addColumn("title", DataTypes.STRING,  256);
					    ds.addColumn("name", DataTypes.STRING,  256);
						ds.addColumn("regdate", DataTypes.DATE, 256);
						ds.addColumn("hit", DataTypes.INT, 256);
						ds.addColumn("pass", DataTypes.STRING, 256);
			
			
			
			
			keyword=new String(keyword.getBytes("iso-8859-1"), "utf-8");
			keyfield=new String(keyfield.getBytes("iso-8859-1"), "utf-8");
			System.out.println(keyword);
			System.out.println(keyfield);
			
			
					BoardVO boardVO;
					HashMap<String, Object> map = new HashMap();
					map.put("keyword",keyword);
					map.put("keyfield", keyfield);
					
				   // strErrorMsg  = 0;
					List<BoardVO> list = null;
					list=this.boardDao.filesearchlist(map);
				   
				   
				   // ResultSet -> Show the Row sets (XML) : browser 
				   for (int i=0; i<list.size(); i++) {
					   int row = ds.newRow();
					   
					   	boardVO = new BoardVO();
					   	boardVO = list.get(i);
					    
					   	ds.set(row, "rnum", boardVO.getRnum());
					   	ds.set(row, "seq", boardVO.getSeq());
					    ds.set(row, "title", boardVO.getTitle());
					    ds.set(row, "name", boardVO.getName());
					    ds.set(row, "regdate",boardVO.getRegdate());
					    ds.set(row, "hit", boardVO.getHit());
					    ds.set(row, "pass", boardVO.getPass());
					   
				   }
					 // for
					pdata.addDataSet(ds);
					
			       // set the ErrorCode and ErrorMsg about success
			       nErrorCode = 0;
			       strErrorMsg = "SUCC";
				    
		
		// save the ErrorCode and ErrorMsg for sending Client
		VariableList varList = pdata.getVariableList();
				
		varList.add("ErrorCode", nErrorCode);
		varList.add("ErrorMsg", strErrorMsg);
				
		// send the result data(XML) to Client
		HttpPlatformResponse res 
		    = new HttpPlatformResponse(response, 
			       									            PlatformType.CONTENT_TYPE_XML,  
			       									            "UTF-8");
		res.setData(pdata); 
		res.sendData();		// Send Data
					
				}
		
		@RequestMapping(value="/board/reply.do")
		public void reply(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="ref",required=false) int gref,@RequestParam(value="indent",required=false) int gindent,@RequestParam(value="step",required=false) int gstep) throws PlatformException{
				
			PlatformData pdata = new PlatformData();

			int nErrorCode = 0;
			String strErrorMsg = "START";
			
			
						HttpPlatformRequest req 
						     = new HttpPlatformRequest(request); 

						req.receiveData();							
						pdata = req.getData();
						
						DataSet ds = pdata.getDataSet("reply");
						
						// DAO
						BoardVO boardVO;
						boardVO = new BoardVO();
						
						boardVO.setRef(gref);
						boardVO.setStep(gstep);
						boardVO.setIndent(gindent);
						boardVO.setTitle(ds.getString(0, "retitle"));
						boardVO.setName(ds.getString(0, "rename"));
						boardVO.setContent(ds.getString(0, "recontent")); 
						boardVO.setPass(ds.getString(0, "repass"));
						
						
						boardDao.reply(boardVO);
						boardDao.updaterep(boardVO);
						boardDao.updatestep(boardVO);
					    // set the ErrorCode and ErrorMsg about success
					    nErrorCode = 0;
					    strErrorMsg = "SUCC";
					    
				
			
			// save the ErrorCode and ErrorMsg for sending Client
			VariableList varList = pdata.getVariableList();
					
			varList.add("ErrorCode", nErrorCode);
			varList.add("ErrorMsg", strErrorMsg);
					
			// send the result data(XML) to Client
			HttpPlatformResponse res 
			    = new HttpPlatformResponse(response, 
				       									            PlatformType.CONTENT_TYPE_XML,  
				       									            "UTF-8");
			res.setData(pdata); 
			res.sendData();		// Send Data
		
		}
		
}	
		
		
