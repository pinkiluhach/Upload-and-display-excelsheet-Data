package com.springboot.display.excelsheet.Repositiry;



import com.springboot.display.excelsheet.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringReadFileRepositiry extends CrudRepository<User,Long> {

}
