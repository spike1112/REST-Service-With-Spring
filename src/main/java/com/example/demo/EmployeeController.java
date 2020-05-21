package com.example.demo;

import org.hibernate.annotations.common.reflection.XMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;

    private final EmployeeModelAssembler assembler;

    public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

/*
    @GetMapping("/employees")
    List<Employee> all() {
        return repository.findAll();
    }*/

    /*
    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return repository.save(newEmployee);
    }*/

    /*
    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) throws URISyntaxException {

	    EntityModel<Employee> entityModel = 
		    assembler.toModel(repository.save(newEmployee));

	    return ResponseEntity
		    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
		    .body(entityModel);
    }
    */

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee,
		    @PathVariable Long id) throws URISyntaxException {

		    Employee updatedEmployee = repository.findById(id)
			    .map(employee -> {
				    employee.setName(newEmployee.getName());
				    employee.setRole(newEmployee.getRole());
				    return repository.save(employee);
			    })
		    .orElseGet(() -> {
			    newEmployee.setId(id);
			    return repository.save(newEmployee);
		    });

		    EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

		    return ResponseEntity
			    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
			    .body(entityModel);
    }



    /*
    @GetMapping("/employees/{id}")
    Employee one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    } */

    /*
    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        return repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
    } */

    /*
    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }*/

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    /*
    @GetMapping("/employees/{id}")
    EntityModel<Employee> one (@PathVariable Long id) {

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return EntityModel.of(employee, linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
                                        linkTo(methodOn(EmployeeController.class).all()).withRel("employee"));
    }*/

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id) {

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);

    }

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {
        List<EntityModel<Employee>> employee = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employee,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }


    /*
    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {

	    List<EntityModel<Employee>> employees = repository.findAll().stream()
		    .map(employee -> EntityModel.of(employee,
					    linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
					    linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
		    .collect(Collectors.toList());

	    return CollectionModel.of(employees,
			    linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }*/

    
}
