package entities;

public class Lesson {
    private long id;
    private int lessonNumber;
    private String weekDay;
    private long idTeacher;
    private long idGroup;
    private long idSubject;

    public Lesson(long id, int lessonNumber, String weekDay, long idTeacher, long idGroup, long idSubject) {
        this.id = id;
        this.lessonNumber = lessonNumber;
        this.weekDay = weekDay;
        this.idTeacher = idTeacher;
        this.idGroup = idGroup;
        this.idSubject = idSubject;
    }
}
