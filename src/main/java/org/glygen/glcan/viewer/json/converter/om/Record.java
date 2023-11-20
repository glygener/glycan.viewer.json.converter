package org.glygen.glcan.viewer.json.converter.om;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Record
{
    private String m_iupacSymbol = null;
    private String m_name = null;
    private boolean m_ignore = false;
    private String m_image = null;
    private String m_url = null;
    private List<Record> m_children = null;
    private Integer m_pubChemCompound = null;
    private String m_description = null;

    @JsonProperty("id")
    public String getIupacSymbol()
    {
        return this.m_iupacSymbol;
    }

    public void setIupacSymbol(String a_iupacSymbol)
    {
        this.m_iupacSymbol = a_iupacSymbol;
    }

    @JsonProperty("name")
    public String getName()
    {
        return this.m_name;
    }

    public void setName(String a_name)
    {
        this.m_name = a_name;
    }

    @JsonProperty("children")
    public List<Record> getChildren()
    {
        return this.m_children;
    }

    public void setChildren(List<Record> a_children)
    {
        this.m_children = a_children;
    }

    @JsonProperty("ignore")
    public boolean isIgnore()
    {
        return this.m_ignore;
    }

    public void setIgnore(boolean a_ignore)
    {
        this.m_ignore = a_ignore;
    }

    @JsonProperty("image")
    public String getImage()
    {
        return this.m_image;
    }

    public void setImage(String a_image)
    {
        this.m_image = a_image;
    }

    @JsonProperty("url")
    public String getUrl()
    {
        return this.m_url;
    }

    public void setUrl(String a_url)
    {
        this.m_url = a_url;
    }

    @JsonProperty("pubChemCompound")
    public Integer getPubChemCompound()
    {
        return this.m_pubChemCompound;
    }

    public void setPubChemCompound(Integer a_pubChemCompound)
    {
        this.m_pubChemCompound = a_pubChemCompound;
    }

    @JsonProperty("description")
    public String getDescription()
    {
        return this.m_description;
    }

    public void setDescription(String a_description)
    {
        this.m_description = a_description;
    }

}
