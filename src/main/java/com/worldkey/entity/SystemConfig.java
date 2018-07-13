package com.worldkey.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author HP
 */
public class SystemConfig implements Serializable {
	
	@NotNull(message="文件路径不能为空")
    private String filesrc;
    private String imgPath;

    private String emailfrom;

    private String emailhost;

    private String emailpassword;

    private String text;

    private String subject;
    private String defaultHeadimg;

    private static final long serialVersionUID = 1L;

    public SystemConfig() {
		super();
	}

	public String getFilesrc() {
		return filesrc;
	}

	public void setFilesrc(String filesrc) {
		this.filesrc = filesrc;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getEmailfrom() {
		return emailfrom;
	}

	public void setEmailfrom(String emailfrom) {
		this.emailfrom = emailfrom;
	}

	public String getEmailhost() {
		return emailhost;
	}

	public void setEmailhost(String emailhost) {
		this.emailhost = emailhost;
	}

	public String getEmailpassword() {
		return emailpassword;
	}

	public void setEmailpassword(String emailpassword) {
		this.emailpassword = emailpassword;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDefaultHeadimg() {
		return defaultHeadimg;
	}

	public void setDefaultHeadimg(String defaultHeadimg) {
		this.defaultHeadimg = defaultHeadimg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((emailfrom == null) ? 0 : emailfrom.hashCode());
		result = prime * result
				+ ((emailhost == null) ? 0 : emailhost.hashCode());
		result = prime * result
				+ ((emailpassword == null) ? 0 : emailpassword.hashCode());
		result = prime * result + ((filesrc == null) ? 0 : filesrc.hashCode());
		result = prime * result + ((imgPath == null) ? 0 : imgPath.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (getClass() != obj.getClass()) {
            return false;
        }
		SystemConfig other = (SystemConfig) obj;
		if (emailfrom == null) {
			if (other.emailfrom != null) {
                return false;
            }
		} else if (!emailfrom.equals(other.emailfrom)) {
            return false;
        }
		if (emailhost == null) {
			if (other.emailhost != null) {
                return false;
            }
		} else if (!emailhost.equals(other.emailhost)) {
            return false;
        }
		if (emailpassword == null) {
			if (other.emailpassword != null) {
                return false;
            }
		} else if (!emailpassword.equals(other.emailpassword)) {
            return false;
        }
		if (filesrc == null) {
			if (other.filesrc != null) {
                return false;
            }
		} else if (!filesrc.equals(other.filesrc)) {
            return false;
        }
		if (imgPath == null) {
			if (other.imgPath != null) {
                return false;
            }
		} else if (!imgPath.equals(other.imgPath)) {
            return false;
        }
		if (subject == null) {
			if (other.subject != null) {
                return false;
            }
		} else if (!subject.equals(other.subject)) {
            return false;
        }
		if (text == null) {
			if (other.text != null) {
                return false;
            }
		} else if (!text.equals(other.text)) {
            return false;
        }
		return true;
	}

	@Override
	public String toString() {
		return "SystemConfig [filesrc=" + filesrc + ", imgPath=" + imgPath
				+ ", emailfrom=" + emailfrom + ", emailhost=" + emailhost
				+ ", emailpassword=" + emailpassword + ", text=" + text
				+ ", subject=" + subject + "]";
	}
    
    
    
    
    
}