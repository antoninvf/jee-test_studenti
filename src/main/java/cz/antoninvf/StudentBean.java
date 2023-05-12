package cz.antoninvf;

import cz.antoninvf.entities.StudentEntity;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Named
@ApplicationScoped
public class StudentBean {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("PRGStudentManager");

    private String id;
    private String firstName;
    private String lastName;
    private String birthDate;
    private int grade;
    private double averageGrade;
    private List<StudentEntity> students;

    public List<StudentEntity> getStudents() {
        EntityManager em = emf.createEntityManager();
        students = em.createQuery("SELECT s FROM StudentEntity s", StudentEntity.class).getResultList();
        em.close();
        return students;
    }

    public double getAverageGrade() {
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT AVG(s.grade) FROM StudentEntity s");
        if (query == null) averageGrade = 0;
        else averageGrade = (double) query.getSingleResult();
        em.close();
        return averageGrade;
    }

    public void addStudent() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();

        StudentEntity user = new StudentEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        if (birthDate == null) birthDate = new Date().toString();
        user.setBirthDate(birthDate);
        user.setGrade(grade);
        em.persist(user);

        et.commit();
        em.close();
    }

    public void editStudent() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();

        StudentEntity user = em.find(StudentEntity.class, Long.parseLong(id));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        if (birthDate == null) birthDate = new Date().toString();
        user.setBirthDate(birthDate);
        user.setGrade(grade);
        em.persist(user);

        et.commit();
        em.close();
    }

    public void deleteStudent(long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();

        StudentEntity user = em.find(StudentEntity.class, id);
        em.remove(user);

        et.commit();
        em.close();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PreDestroy
    public void onDestroy() {
        emf.close();
    }
}
