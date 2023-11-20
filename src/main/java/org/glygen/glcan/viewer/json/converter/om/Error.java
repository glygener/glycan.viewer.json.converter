package org.glygen.glcan.viewer.json.converter.om;

public class Error 
{
	private Integer m_row = null;
	private String m_message = null;
	public Integer getRow() {
		return m_row;
	}
	public void setRow(Integer a_row) {
		m_row = a_row;
	}
	public String getMessage() {
		return m_message;
	}
	public void setMessage(String a_message) {
		m_message = a_message;
	}
}
