package com.zhaoxin.football;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "SaveTable")
public class SaveTable {
	@Column(name = "id",isId = true,autoGen = true)
	public int id;
	@Column(name = "name")
	public String name;
	@Column(name = "json")
	public String json;
	@Column(name = "Pheight")
	public int Pheight;
	@Column(name = "Pwidth")
	public int Pwidth;
}
