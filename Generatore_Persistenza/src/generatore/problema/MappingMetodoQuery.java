package generatore.problema;

import java.util.*;

public class MappingMetodoQuery {

	public String tipoRitorno;
	public String nomeMetodo;
	public String byAttributi;
	
	public List<String> m1;
	public List<String> m2;
	public List<String> m3;
	public List<String> m4a;
	public List<String> m4b;
	public List<String> m4c;
	public List<String> m4d;
	public List<String> m4e;
	public List<String> m4f;
	public List<String> m5;
	public List<String> m6;
	public List<String> m7;
	
	public MappingMetodoQuery() {
		m1=new LinkedList<String>();
		m2=new LinkedList<String>();
		m3=new LinkedList<String>();
		m4a=new LinkedList<String>();
		m4b=new LinkedList<String>();
		m4c=new LinkedList<String>();
		m4d=new LinkedList<String>();
		m4e=new LinkedList<String>();
		m4f=new LinkedList<String>();
		m5=new LinkedList<String>();
		m6=new LinkedList<String>();
		m7=new LinkedList<String>();	
	}

	public MappingMetodoQuery(String tipoRitorno, String nomeMetodo, String byAttributi) {
		this();
		this.tipoRitorno = tipoRitorno;
		this.nomeMetodo = nomeMetodo;
		this.byAttributi = byAttributi;
	}

	public String getTipoRitorno() {
		return tipoRitorno;
	}

	public void setTipoRitorno(String tipoRitorno) {
		this.tipoRitorno = tipoRitorno;
	}

	public String getNomeMetodo() {
		return nomeMetodo;
	}

	public void setNomeMetodo(String nomeMetodo) {
		this.nomeMetodo = nomeMetodo;
	}

	public String getByAttributi() {
		return byAttributi;
	}

	public void setByAttributi(String byAttributi) {
		this.byAttributi = byAttributi;
	}

	public List<String> getM1() {
		return m1;
	}

	public void setM1(List<String> m1) {
		this.m1 = m1;
	}

	public List<String> getM2() {
		return m2;
	}

	public void setM2(List<String> m2) {
		this.m2 = m2;
	}

	public List<String> getM3() {
		return m3;
	}

	public void setM3(List<String> m3) {
		this.m3 = m3;
	}

	public List<String> getM4a() {
		return m4a;
	}

	public void setM4a(List<String> m4a) {
		this.m4a = m4a;
	}

	public List<String> getM4b() {
		return m4b;
	}

	public void setM4b(List<String> m4b) {
		this.m4b = m4b;
	}

	public List<String> getM4c() {
		return m4c;
	}

	public void setM4c(List<String> m4c) {
		this.m4c = m4c;
	}

	public List<String> getM4d() {
		return m4d;
	}

	public void setM4d(List<String> m4d) {
		this.m4d = m4d;
	}

	public List<String> getM4e() {
		return m4e;
	}

	public void setM4e(List<String> m4e) {
		this.m4e = m4e;
	}

	public List<String> getM4f() {
		return m4f;
	}

	public void setM4f(List<String> m4f) {
		this.m4f = m4f;
	}

	public List<String> getM5() {
		return m5;
	}

	public void setM5(List<String> m5) {
		this.m5 = m5;
	}

	public List<String> getM6() {
		return m6;
	}

	public void setM6(List<String> m6) {
		this.m6 = m6;
	}

	public List<String> getM7() {
		return m7;
	}

	public void setM7(List<String> m7) {
		this.m7 = m7;
	}
	
	
	
}
