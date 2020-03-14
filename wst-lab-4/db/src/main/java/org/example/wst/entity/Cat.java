package org.example.wst.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Cat {

    private Integer id;
    private String name;
    private Integer age;
    private String breed;
    private Integer weight;

    public Cat() {
    }

    public Cat(Integer id, String name, Integer age, String breed, Integer weight) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.weight = weight;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", breed='" + breed + '\'' +
                ", weight=" + weight +
                '}';
    }
}
