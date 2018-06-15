package liudrcx.entitystub;

import static liudrcx.entitystub.EntityStub.*;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class EntityStubTest {
  
  @Test
  public void testSimpleCreate() {
    Student stu = 
      createEntity(
        Student.class, 
        entity(
          field("name", "Allen"),
          field("age", 25)
      ));
    
    assertEquals(stu.getName(), "Allen");
    assertEquals(stu.getAge(), 25);
  }
  
  @Test
  public void testSimpleAssert() {
    Student stu = new Student();
    stu.setName("Allen");
    stu.setAge(25);
    
    assertEntity(
      stu, 
      entity(
        field("name", "Allen"),
        field("age", 25)
      )
    );
  }
  
  @Test
  public void testRefInlineCreate() {
    Student stu = 
      createEntity(
        Student.class,
        entity(
          field("name", "Allen"),
          field("age", 25),
          field(
            "classroom", 
            entity(
              field("name", "cs1")
            ) 
          )
        )
      );
    
    assertEquals(stu.getName(), "Allen");
    assertEquals(stu.getAge(), 25);
    assertEquals(stu.getClassroom().getName(), "cs1");
  }
    
  @Test
  public void testRefSeparateCreate() {
    Classroom room = 
      createEntity(
        Classroom.class, 
        entity(
          field("name", "cs1")
        )
      );
    
    Student stu = 
      createEntity(
        Student.class,
        entity(
          field("name", "Allen"),
          field("age", 25),
          field("classroom", room)
        )
      );
    
    assertEquals(stu.getName(), "Allen");
    assertEquals(stu.getAge(), 25);
    assertEquals(stu.getClassroom().getName(), "cs1");
  }
  
  @Test
  public void testRefAssert() {
    Classroom room = new Classroom();
    room.setName("cs1");
    
    Student stu = new Student();
    stu.setName("Allen");
    stu.setAge(27);
    stu.setClassroom(room);
    
    assertEntity(
      stu, 
      entity(
        field("name", "Allen"),
        field("age", 27),
        field(
          "classroom", 
          entity(
            field("name", "cs1")
          )
        )
      )
    );
  }
  
  @Test
  public void testMultiCreate() {
    List<Student> stus = 
      createEntities(
        Student.class, 
        entities(
          entity(
            field("name", "Allen"),
            field("age", 20),
            field(
              "classroom", 
              entity(
                field("name", "cs1")
              ) 
            )
          ),
          entity(
            field("name", "Tom"),
            field("age", 10)
          )
        )
      );
    
    assertEquals(stus.size(), 2);
    assertEquals(stus.get(0).getName(), "Allen");
    assertEquals(stus.get(0).getAge(), 20);
    assertEquals(stus.get(0).getClassroom().getName(), "cs1");
    
    assertEquals(stus.get(1).getName(), "Tom");
    assertEquals(stus.get(1).getAge(), 10);
  }
  
  @Test
  public void testMultiAssert() {
    Classroom room = new Classroom();
    room.setName("cs1");
    
    Student stu1 = new Student();
    stu1.setName("Allen");
    stu1.setAge(20);
    stu1.setClassroom(room);
    
    Student stu2 = new Student();
    stu2.setName("Tom");
    stu2.setAge(25);
    
    List<Student> stus = new ArrayList<>();
    stus.add(stu1);
    stus.add(stu2);
    
    assertEntities(
      stus, 
      entities(
        entity(
          field("name", "Allen"),
          field("age", 20),
          field(
            "classroom", 
            entity(
              field("name", "cs1")
            )
          )
        ),
        entity(
          field("name", "Tom"),
          field("age", 25)
        )
      )
    );
  }
  
  @Test
  public void testMultiRefsCreate() {
    Student stu =
      createEntity(
        Student.class, 
        entity(
          field("name", "Allen"),
          field("age", 25),
          field(
            "classroom", 
            entity(
              field("name", "cs1")
            )
          ),
          field(
            "groups", 
            entities(
              entity(
                field("name", "English"),
                field("size", 10)
              ),
              entity(
                field("name", "football"),
                field("size", 20)
              )
            )
          )
        )
      );
    
    assertEquals(stu.getName(), "Allen");
    assertEquals(stu.getAge(), 25);
    assertEquals(stu.getClassroom().getName(), "cs1");
    
    assertEquals(stu.getGroups().size(), 2);
    assertEquals(stu.getGroups().get(0).getName(), "English");
    assertEquals(stu.getGroups().get(0).getSize(), 10);
    
    assertEquals(stu.getGroups().get(1).getName(), "football");
    assertEquals(stu.getGroups().get(1).getSize(), 20);
  }
  
  @Test
  public void testMultiRefsAssert() {
    Classroom room = new Classroom();
    room.setName("cs1");
    
    Group g1 = new Group();
    g1.setName("English");
    g1.setSize(15);
    
    Group g2 = new Group();
    g2.setName("football");
    g2.setSize(30);
    
    Student stu = new Student();
    stu.setName("Allen");
    stu.setAge(20);
    stu.setClassroom(room);
    stu.setGroups(Arrays.asList(g1, g2));
    
    assertEntity(
      stu, 
      entity(
        field("name", "Allen"),
        field("age", 20),
        field(
          "classroom",
          entity(
            field("name", "cs1")
          )
        ),
        field(
          "groups", 
          entities(
            entity(
              field("name", "English"),
              field("size", 15)
            ),
            entity(
              field("name", "football"),
              field("size", 30)
            )
          )
        )
      )
    );
  }
  
  @Test
  public void testIntegration() {
    List<Student> stus = 
      createEntities(
        Student.class, 
        entities(
          entity(
            field("name", "Allen"),
            field("age", 20),
            field(
              "classroom", 
              entity(
                field("name", "cs1")
              )
            ),
            field(
              "groups", 
              entities(
                entity(
                  field("name", "English"),
                  field("size", 10),
                  field(
                    "teachers", 
                    entities(
                      entity(
                        field("name", "Tom")
                      ),
                      entity(
                        field("name", "Peter")
                      )
                    )
                  )
                ),
                entity(
                  field("name", "football"),
                  field("size", 10)
                )
              ))
          ),
          entity(
            field("name", "Mike"),
            field("age", 20)
          )
        )
      );
    
    assertEntities(
      stus, 
      entities(
        entity(
          field("name", "Allen"),
          field("age", 20),
          field(
            "classroom", 
            entity(
              field("name", "cs1")
            )
          ),
          field(
            "groups", 
            entities(
              entity(
                field("name", "English"),
                field("size", 10),
                field(
                  "teachers", 
                  entities(
                    entity(
                      field("name", "Tom")
                    ),
                    entity(
                      field("name", "Peter")
                    )
                  )
                )
              ),
              entity(
                field("name", "football"),
                field("size", 10)
              )
            ))
        ),
        entity(
          field("name", "Mike"),
          field("age", 20)
        )
      )
    );
  }
  
  
  public static class Student {
    private String name;
    
    private int age;
    
    private Classroom classroom;
    
    private List<Group> groups = new ArrayList<>();
    
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }

    public Classroom getClassroom() {
      return classroom;
    }

    public void setClassroom(Classroom classroom) {
      this.classroom = classroom;
    }

    public List<Group> getGroups() {
      return groups;
    }

    public void setGroups(List<Group> groups) {
      this.groups = groups;
    }
  }
  
  public static class Classroom {
    private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
  
  public static class Group {
    private String name;
    private int size;
    
    private List<Teacher> teachers = new ArrayList<>();
    
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public int getSize() {
      return size;
    }
    public void setSize(int size) {
      this.size = size;
    }
    public List<Teacher> getTeachers() {
      return teachers;
    }
    public void setTeachers(List<Teacher> teachers) {
      this.teachers = teachers;
    }
  }
  
  public static class Teacher {
    private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
