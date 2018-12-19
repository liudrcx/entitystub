# entitystub

This is a tool to create and assert Java POJO entities for testing purpose. 

Please refer to EntityStubTest.java to get more examples.


```
Student stu = 
      createEntity(
        Student.class, 
        entity(
          field("name", "Allen"),
          field("age", 25)
      ));
    
    assertEquals(stu.getName(), "Allen");
    assertEquals(stu.getAge(), 25);
   
```

