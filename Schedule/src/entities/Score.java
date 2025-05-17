package entities;

public class Score {
    private long id;
    private long idStudent;
    private long idSubject;
    private int value;

    public Score(long id, long idStudent, long idSubject, int value) {
        this.id = id;
        this.idStudent = idStudent;
        this.idSubject = idSubject;
        this.value = value;
    }
}