package org.glygen.glcan.viewer.json.converter.om;

import java.util.ArrayList;
import java.util.List;

public class FileReadingResult 
{
	private List<Record> m_records = new ArrayList<>();
	private List<Error> m_errors = new ArrayList<>();
	public List<Record> getRecords() {
		return m_records;
	}
	public void setRecords(List<Record> a_records) {
		m_records = a_records;
	}
	public List<Error> getErrors() {
		return m_errors;
	}
	public void setErrors(List<Error> a_errors) {
		m_errors = a_errors;
	}
}
