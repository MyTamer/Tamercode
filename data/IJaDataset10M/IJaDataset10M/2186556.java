package org.pojodm.party;

import org.pojodm.entity.Entity;
import org.pojodm.util.DateTimeUtils;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author Les Hazlewood
 */
public class Person extends Party {

    private Gender gender;

    private String nameSalutation;

    private String givenName;

    private String middleNames;

    private String surname;

    private String nameSuffix;

    private Date dateOfBirth;

    private String title;

    public Person() {
    }

    public Person(String givenName, String surname) {
        setGivenName(givenName);
        setSurname(surname);
    }

    public Person(String givenName, String middleNames, String surname) {
        setGivenName(givenName);
        setMiddleNames(middleNames);
        setSurname(surname);
    }

    /**
     * Alias for getId();
     *
     * @return
     */
    public Long getPersonId() {
        return getId();
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getNameSalutation() {
        return nameSalutation;
    }

    public void setNameSalutation(String nameSalutation) {
        this.nameSalutation = nameSalutation;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getMiddleNames() {
        return middleNames;
    }

    public void setMiddleNames(String middleNames) {
        this.middleNames = middleNames;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = DateTimeUtils.midnight(dateOfBirth);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns this person's givenName and surname (i.e. first and last)
     *
     * @return this person's givenName and surname (i.e. first and last)
     */
    public String getSimpleName() {
        return getSimpleName(true);
    }

    public String getSimpleName(boolean givenNameFirst) {
        StringBuffer sb = new StringBuffer();
        String first;
        String last;
        if (givenNameFirst) {
            first = getGivenName();
            last = getSurname();
        } else {
            first = getSurname();
            last = getGivenName();
        }
        if (first != null) {
            sb.append(first);
        }
        if (last != null) {
            if (first != null) {
                sb.append(" ");
            }
            sb.append(last);
        }
        return sb.toString();
    }

    /**
     * Returns this person's full name, with all name components (salutation, givenName, ..., etc).
     *
     * @return this person's full name, with all name components (salutation, givenName, ..., etc).
     */
    public String getFullName() {
        StringBuffer sb = new StringBuffer();
        if (getNameSalutation() != null) {
            sb.append(getNameSalutation());
        }
        if (getGivenName() != null) {
            sb.append(" ").append(getGivenName());
        }
        if (getMiddleNames() != null) {
            sb.append(" ").append(getMiddleNames());
        }
        if (getSurname() != null) {
            sb.append(" ").append(getSurname());
        }
        if (getNameSuffix() != null) {
            sb.append(" ").append(getNameSuffix());
        }
        return sb.toString().trim();
    }

    public StringBuffer toStringBuffer() {
        StringBuffer sb = super.toStringBuffer();
        sb.append(",gender=").append(getGender());
        sb.append(",name=").append(getFullName());
        Date dob = getDateOfBirth();
        if (dob != null) {
            DateFormat df = DateFormat.getInstance();
            sb.append(",dateOfBirth=[").append(df.format(dob.getTime())).append("]");
        }
        sb.append(",title=").append(getTitle());
        return sb;
    }

    public boolean onEquals(Entity e) {
        if (e instanceof Person) {
            Person p = (Person) e;
            return (givenName == null ? p.getGivenName() == null : givenName.equals(p.getGivenName())) && (surname == null ? p.getSurname() == null : surname.equals(p.getSurname())) && (middleNames == null ? p.getMiddleNames() == null : middleNames.equals(p.getMiddleNames())) && (dateOfBirth == null ? p.getDateOfBirth() == null : dateOfBirth.equals(p.getDateOfBirth())) && (gender == null ? p.getGender() == null : gender.equals(p.getGender())) && (nameSalutation == null ? p.getNameSalutation() == null : nameSalutation.equals(p.getNameSalutation())) && (nameSuffix == null ? p.getNameSuffix() == null : nameSuffix.equals(p.getNameSuffix())) && (title == null ? p.getTitle() == null : title.equals(p.getTitle()));
        }
        return false;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (gender != null ? gender.hashCode() : 0);
        result = 29 * result + (nameSalutation != null ? nameSalutation.hashCode() : 0);
        result = 29 * result + (givenName != null ? givenName.hashCode() : 0);
        result = 29 * result + (middleNames != null ? middleNames.hashCode() : 0);
        result = 29 * result + (surname != null ? surname.hashCode() : 0);
        result = 29 * result + (nameSuffix != null ? nameSuffix.hashCode() : 0);
        result = 29 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 29 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    @SuppressWarnings({ "CloneDoesntDeclareCloneNotSupportedException" })
    public Object clone() {
        Person clone = (Person) super.clone();
        clone.setGender(getGender());
        clone.setNameSalutation(getNameSalutation());
        clone.setGivenName(getGivenName());
        clone.setMiddleNames(getMiddleNames());
        clone.setSurname(getSurname());
        clone.setNameSuffix(getNameSuffix());
        Date dob = getDateOfBirth();
        if (dob != null) {
            clone.setDateOfBirth((Date) dob.clone());
        }
        clone.setTitle(getTitle());
        return clone;
    }

    public void accept(PartyVisitor visitor) {
        visitor.visit(this);
    }
}
