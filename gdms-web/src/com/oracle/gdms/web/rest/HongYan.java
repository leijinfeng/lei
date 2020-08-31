package com.oracle.gdms.web.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.alibaba.fastjson.JSONObject;
import com.oracle.gdms.entity.GoodsModel;
import com.oracle.gdms.entity.GoodsType;
import com.oracle.gdms.entity.ResponseEntity;
import com.oracle.gdms.service.GoodsService;
import com.oracle.gdms.util.Factory;

@Path("/hongyan")
public class HongYan {
	
	@Path("/sing")
	@GET
	public String sing() {
		System.out.print("sing");
		return "HELLO";
	}
	
	@Path("/sing/ci") //rest/hongyan/sing/ci?name=""
	@GET
	public String singOne(@QueryParam("name") String name) {
		System.out.println("��ʣ�"+name);
		return "ok";
	}
	
	@Path("/push/one") 
	@POST
	public String push(@FormParam("name") String name, @FormParam("sex") String sex) {
		System.out.println("��Ʒ���ƣ�"+name);
		System.out.println("��Ʒ���ƣ�"+sex);
		return "form";
	}
	
	@Path("/push/json")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String pushJson(String jsonparam) {
		System.out.println(jsonparam);
		JSONObject j = JSONObject.parseObject(jsonparam);
		String name = j.getString("name");
		String sex = j.getString("sex");
		String age = j.getString("age");
		System.out.println("name:"+name);
		System.out.println("sex:"+sex);
		System.out.println("age:"+age);
		return "json";
	}
	
	@Path("/goods/update/type")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)  //�涨���ص�ΪJSON����
	public String updateGoodsType(String jsonparam) {
		JSONObject j = JSONObject.parseObject(jsonparam);
		String goodsid = j.getString("goodsid");
		String gtid = j.getString("gtid");
		System.out.println("Ҫ�޸ĵ���ƷID=" + goodsid + "Ŀ�����ID=" + gtid);
		
		GoodsService service = (GoodsService) Factory.getInstance().getObject("update.goods.type");
		int count = service.updataGoodsType(goodsid,gtid);
		if (count > 0) {
			j.put("code", 0);
			j.put("msg", "���³ɹ�");
		} else {
			j.put("code", 1);
			j.put("msg", "������Ʒʧ��");
		}
		
		return j.toJSONString();
	}
	
	
	
	@Path("/goods/push/bytype")
	@POST
	@Produces(MediaType.APPLICATION_JSON)	
	@Consumes(MediaType.APPLICATION_JSON)  //�涨���صĽ��ΪJSON����
	public List<GoodsModel> pushGoodsType(GoodsType type){
		List<GoodsModel> list = null;
		
		GoodsService service = (GoodsService) Factory.getInstance().getObject("goods.sevice.impl");
		list = service.findByType(type.getGtid());
		System.out.println("size="+list.size());
		return list;
	}
	
	
	@Path("/goods/push/one")
	@POST
	@Produces(MediaType.APPLICATION_JSON)	
	@Consumes(MediaType.APPLICATION_JSON)  //�涨���صĽ��ΪJSON����
	public ResponseEntity pushGoodsOne(String jsonparam) {
		
		
		ResponseEntity r = new ResponseEntity();
		
		try {
			//{"goods":{"goodsid": 42, "name":"��Ȫˮ","price":"3.5"}}
			JSONObject j = JSONObject.parseObject(jsonparam);
			
			String gs = j.getString("goods");
			GoodsModel goods = JSONObject.parseObject(gs, GoodsModel.class);
			System.out.println("������յ��ˣ�");
			System.out.println("��ƷID��" + goods.getGoodsid());
			System.out.println("��Ʒ���ƣ�" + goods.getName());
			System.out.println("������ַ��" + goods.getArea());
			r.setCode(0);
			r.setMessage("���ͳɹ�");
		} catch (Exception e) {
			e.printStackTrace();
			r.setCode(1184);
			r.setMessage("����ʧ��,��Ʒ���ݲ��Ϸ���" + jsonparam);
		}
		
		return r;
		
	}
}
