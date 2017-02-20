package dsergeyev.example.models.user;

import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import dsergeyev.example.models.converters.ColorConverter;
import dsergeyev.example.models.converters.LocalDateToSqlDateAttributeConverter;
import dsergeyev.example.models.converters.LocalDateToStringAttributeConverter;
import dsergeyev.example.models.converters.LocalDateTimeAttributeConverter;

@Entity
@Table(name = "user")
public class User {

	private long id;
	private String firstName;
	private String secondName;
	
	private LocalDate birthDate1;
	private LocalDate birthDate2;
	private LocalDate birthDate3;
	
	private LocalDateTime createDate;
	private LocalDateTime updateDate;
	
	private Color color;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID", nullable=false, unique=true)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name="FISRT_NAME", nullable=false)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name="SECOND_NAME", nullable=false)
	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	@Column(name = "BIRTH_DATE1", nullable=true, unique=false)
	@Type(type="org.hibernate.type.LocalDateType")
	public LocalDate getBirthDate1() {
		return birthDate1;
	}
	
	public void setBirthDate1(LocalDate birthDate) {
		this.birthDate1 = birthDate;
	}
	
	@Column(name="BIRTH_DATE2", nullable=true, unique=false)
	@Convert(converter = LocalDateToSqlDateAttributeConverter.class)
	public LocalDate getBirthDate2() {
		return birthDate2;
	}

	public void setBirthDate2(LocalDate strCreateDate) {
		this.birthDate2 = strCreateDate;
	}

	@Column(name="BIRTH_DATE3", nullable=false, unique=false)
	@Convert(converter = LocalDateToStringAttributeConverter.class)
	public LocalDate getBirthDate3() {
		return birthDate3;
	}

	public void setBirthDate3(LocalDate birthDate3) {
		this.birthDate3 = birthDate3;
	}
	
	@Column(name = "CREATE_DATE")
	@Type(type="org.hibernate.type.LocalDateTimeType")
	public LocalDateTime getCreateDate() {
		return createDate;
	}	

	public void setCreateDate(LocalDateTime startDate) {
		this.createDate = startDate;
	}
	
	@Column(name = "UPDATE_DATE")
	@Convert(converter = LocalDateTimeAttributeConverter.class)
	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}
	
	@Column(name="COLOR", nullable=true, unique=false)
	@Convert(converter = ColorConverter.class)
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
