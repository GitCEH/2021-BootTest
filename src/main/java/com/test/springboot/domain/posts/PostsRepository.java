package com.test.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PostsRepository extends JpaRepository<Posts,Long> {

    //보통 ibatis나 MyBatis 등에서 Dao라고 불리는 DB Layer 접근자는
    //JPA에서는 Repository라고 부르며 인터페이스로 생성함.
    //단순히 인터페이스를 생성 후, JpaRepository<Entity클래스,PK타입>를 상속하면 기본적인 CRUD 메소드가 생성됨

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
}
