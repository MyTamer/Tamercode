package net.sf.jpasecurity.sample.elearning.domain.course;

import static org.apache.commons.lang.Validate.notNull;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import net.sf.jpasecurity.AccessType;
import net.sf.jpasecurity.sample.elearning.domain.Content;
import net.sf.jpasecurity.sample.elearning.domain.Lesson;
import net.sf.jpasecurity.sample.elearning.domain.Title;
import net.sf.jpasecurity.security.Permit;
import net.sf.jpasecurity.security.PermitAny;

/**
 * This class is package private so that it can be accessed solely from within the {@link CourseAggregate}
 * to maintain integrity.
 *
 * @author Arne Limburg - open knowledge GmbH (arne.limburg@openknowledge.de)
 */
@Entity
@PermitAny({ @Permit(rule = "course.lecturer.name.nick = CURRENT_PRINCIPAL"), @Permit(access = AccessType.READ) })
@Table(name = "LESSON")
class LessonEntity implements Lesson {

    @Id
    @GeneratedValue
    private Integer id;

    private int number;

    @Embedded
    @AttributeOverride(name = "text", column = @Column(name = "TITLE"))
    private Title title;

    @Embedded
    @AttributeOverride(name = "text", column = @Column(name = "CONTENT"))
    private Content content;

    @ManyToOne
    private CourseAggregate course;

    protected LessonEntity() {
    }

    protected LessonEntity(CourseAggregate course, String title, String content) {
        this(course, new Title(title), new Content(content));
    }

    public LessonEntity(CourseAggregate course, Title title, Content content) {
        notNull(course, "course may not be null");
        notNull(title, "title may not be null");
        notNull(content, "content may not be null");
        this.course = course;
        this.number = course.getLessons().size();
        this.title = title;
        this.content = content;
        course.addLesson(this);
    }

    public int getId() {
        return id == null ? -1 : id;
    }

    public void setNumber(int number) {
        if (this.number != number) {
            throw new IllegalStateException("cannot change number from " + this.number + " to " + number);
        }
    }

    public int getNumber() {
        return number;
    }

    public Title getTitle() {
        return title;
    }

    public CourseAggregate getCourse() {
        return course;
    }

    public Content getContent() {
        return content;
    }

    public int hashCode() {
        if (id == null) {
            return System.identityHashCode(this);
        }
        return getId();
    }

    public boolean equals(Object object) {
        if (!(object instanceof LessonEntity)) {
            return false;
        }
        if (id == null) {
            return this == object;
        }
        LessonEntity lesson = (LessonEntity) object;
        return id.equals(lesson.getId());
    }
}
