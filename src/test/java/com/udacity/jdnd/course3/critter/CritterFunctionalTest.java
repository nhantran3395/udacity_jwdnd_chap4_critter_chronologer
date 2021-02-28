package com.udacity.jdnd.course3.critter;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.udacity.jdnd.course3.critter.DTO.*;
import com.udacity.jdnd.course3.critter.controller.UserController;
import com.udacity.jdnd.course3.critter.controller.PetController;
import com.udacity.jdnd.course3.critter.model.model_enum.PetType;
import com.udacity.jdnd.course3.critter.controller.ScheduleController;
import com.udacity.jdnd.course3.critter.model.model_enum.SkillEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CritterFunctionalTest {

    @Autowired
    private UserController userController;

    @Autowired
    private PetController petController;

    @Autowired
    private ScheduleController scheduleController;

    @Autowired
    private MockMvc mvc;

    @Test
    public void checkCreateCustomerSuccessful() throws Exception {
        mvc.perform(post("/user/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getCreateCustomerRequestBody("bernardharv","Bernard Harvey","(220)-574-0718"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("bernardharv"))
                .andExpect(jsonPath("$.name").value("Bernard Harvey"))
                .andExpect(jsonPath("$.phoneNumber").value("(220)-574-0718"));

        mvc.perform(get("/user/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("bernardharv"))
                .andExpect(jsonPath("$.name").value("Bernard Harvey"))
                .andExpect(jsonPath("$.phoneNumber").value("(220)-574-0718"));
    }

    @Test
    public void checkCreateEmployeeSuccessful() throws Exception {
        mvc.perform(post("/user/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getCreateEmployeeRequestBody("bernardharv","Bernard Harvey",
                        List.of("\"" + "WALKING" + "\""),List.of("\"" + "MONDAY" + "\"")))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("bernardharv"))
                .andExpect(jsonPath("$.name").value("Bernard Harvey"));

        mvc.perform(get("/user/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("bernardharv"))
                .andExpect(jsonPath("$.name").value("Bernard Harvey"))
                .andExpect(jsonPath("$.skills.[0]").value("WALKING"))
                .andExpect(jsonPath("$.daysAvailable.[0]").value("MONDAY"));

    }

    @Test
    public void checkCreatePetAndAssignToCustomerSuccessful() throws Exception {
        mvc.perform(post("/user/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getCreateCustomerRequestBody("bernardharv","Bernard Harvey","(220)-574-0718"))
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform(post("/pet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getCreatePetRequestBody("DOG","Tucker",1L,"2020-02-27"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("DOG"))
                .andExpect(jsonPath("$.name").value("Tucker"))
                .andExpect(jsonPath("$.ownerId").value(1));

        mvc.perform(get("/user/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("bernardharv"))
                .andExpect(jsonPath("$.name").value("Bernard Harvey"))
                .andExpect(jsonPath("$.phoneNumber").value("(220)-574-0718"))
                .andExpect(jsonPath("$.petIds",hasSize(1)))
                .andExpect(jsonPath("$.petIds.[0]").value(1));
    }

    @Test
    public void checkChangeOwnerOfPetSuccessful() throws Exception {
        mvc.perform(post("/user/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getCreateCustomerRequestBody("bernardharv","Bernard Harvey","(220)-574-0718"))
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform(post("/user/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getCreateCustomerRequestBody("joshuajordan","Joshua Jordan","(554)-374-2187"))
                .accept(MediaType.APPLICATION_JSON));


        mvc.perform(post("/pet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getCreatePetRequestBody("DOG","Tucker",1L,"2020-02-27"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("DOG"))
                .andExpect(jsonPath("$.name").value("Tucker"))
                .andExpect(jsonPath("$.ownerId").value(1));

        //change the owner of Tucker from Bernard to Joshua
        mvc.perform(put("/pet/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(2))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //check that Joshua own Tucker now
        mvc.perform(get("/user/customer/pet/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.username").value("joshuajordan"))
                .andExpect(jsonPath("$.name").value("Joshua Jordan"))
                .andExpect(jsonPath("$.phoneNumber").value("(554)-374-2187"))
                .andExpect(jsonPath("$.petIds",hasSize(1)))
                .andExpect(jsonPath("$.petIds.[0]").value(1));

        //check that Tucker actually belong to Joshua
        mvc.perform(get("/pet/owner/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].type").value("DOG"))
                .andExpect(jsonPath("$.[0].name").value("Tucker"))
                .andExpect(jsonPath("$.[0].ownerId").value(2));

    }

//    @Test
//    public void testFindPetsByOwner() {
//        CustomerDTO customerDTO = createCustomerDTO();
//        CustomerDTO newCustomer = userController.saveCustomer(customerDTO);
//
//        PetDTO petDTO = createPetDTO();
//        petDTO.setOwnerId(newCustomer.getId());
//        PetDTO newPet = petController.savePet(petDTO);
//        petDTO.setType(PetType.DOG);
//        petDTO.setName("DogName");
//        PetDTO newPet2 = petController.savePet(petDTO);
//
//        List<PetDTO> pets = petController.getPetsByOwner(newCustomer.getId());
//        Assertions.assertEquals(pets.size(), 2);
//        Assertions.assertEquals(pets.get(0).getOwnerId(), newCustomer.getId());
//        Assertions.assertEquals(pets.get(0).getId(), newPet.getId());
//    }
//
//    @Test
//    public void testFindOwnerByPet() {
//        CustomerDTO customerDTO = createCustomerDTO();
//        CustomerDTO newCustomer = userController.saveCustomer(customerDTO);
//
//        PetDTO petDTO = createPetDTO();
//        petDTO.setOwnerId(newCustomer.getId());
//        PetDTO newPet = petController.savePet(petDTO);
//
//        CustomerDTO owner = userController.getOwnerByPet(newPet.getId());
//        Assertions.assertEquals(owner.getId(), newCustomer.getId());
//        Assertions.assertEquals(owner.getPetIds().get(0), newPet.getId());
//    }
//
//    @Test
//    public void testChangeEmployeeAvailability() throws Exception {
//        EmployeeDTO employeeDTO = createEmployeeDTO();
//        EmployeeDTO emp1 = userController.saveEmployee(employeeDTO);
//        Assertions.assertNull(emp1.getDaysAvailable());
//
//        Set<DayOfWeek> availability = Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY);
//        userController.setAvailability(availability, emp1.getId());
//
//        EmployeeDTO emp2 = userController.getEmployee(emp1.getId());
//        Assertions.assertEquals(availability, emp2.getDaysAvailable());
//    }
//
//    @Test
//    public void testFindEmployeesByServiceAndTime() {
//        EmployeeDTO emp1 = createEmployeeDTO();
//        EmployeeDTO emp2 = createEmployeeDTO();
//        EmployeeDTO emp3 = createEmployeeDTO();
//
//        emp1.setDaysAvailable(Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
//        emp2.setDaysAvailable(Sets.newHashSet(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
//        emp3.setDaysAvailable(Sets.newHashSet(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));
//
//        emp1.setSkills(Sets.newHashSet(SkillEnum.FEEDING, SkillEnum.PETTING));
//        emp2.setSkills(Sets.newHashSet(SkillEnum.PETTING, SkillEnum.WALKING));
//        emp3.setSkills(Sets.newHashSet(SkillEnum.WALKING, SkillEnum.SHAVING));
//
//        EmployeeDTO emp1n = userController.saveEmployee(emp1);
//        EmployeeDTO emp2n = userController.saveEmployee(emp2);
//        EmployeeDTO emp3n = userController.saveEmployee(emp3);
//
//        //make a request that matches employee 1 or 2
//        EmployeeRequestDTO er1 = new EmployeeRequestDTO();
//        er1.setDate(LocalDate.of(2019, 12, 25)); //wednesday
//        er1.setSkills(Sets.newHashSet(SkillEnum.PETTING));
//
//        Set<Long> eIds1 = userController.findEmployeesForService(er1).stream().map(EmployeeDTO::getId).collect(Collectors.toSet());
//        Set<Long> eIds1expected = Sets.newHashSet(emp1n.getId(), emp2n.getId());
//        Assertions.assertEquals(eIds1, eIds1expected);
//
//        //make a request that matches only employee 3
//        EmployeeRequestDTO er2 = new EmployeeRequestDTO();
//        er2.setDate(LocalDate.of(2019, 12, 27)); //friday
//        er2.setSkills(Sets.newHashSet(SkillEnum.WALKING, SkillEnum.SHAVING));
//
//        Set<Long> eIds2 = userController.findEmployeesForService(er2).stream().map(EmployeeDTO::getId).collect(Collectors.toSet());
//        Set<Long> eIds2expected = Sets.newHashSet(emp3n.getId());
//        Assertions.assertEquals(eIds2, eIds2expected);
//    }
//
//    @Test
//    public void testSchedulePetsForServiceWithEmployee() {
//        EmployeeDTO employeeTemp = createEmployeeDTO();
//        employeeTemp.setDaysAvailable(Sets.newHashSet(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY));
//        EmployeeDTO employeeDTO = userController.saveEmployee(employeeTemp);
//        CustomerDTO customerDTO = userController.saveCustomer(createCustomerDTO());
//        PetDTO petTemp = createPetDTO();
//        petTemp.setOwnerId(customerDTO.getId());
//        PetDTO petDTO = petController.savePet(petTemp);
//
//        LocalDate date = LocalDate.of(2019, 12, 25);
//        List<Long> petList = Lists.newArrayList(petDTO.getId());
//        List<Long> employeeList = Lists.newArrayList(employeeDTO.getId());
//        Set<SkillEnum> skillSet =  Sets.newHashSet(SkillEnum.PETTING);
//
//        scheduleController.createSchedule(createScheduleDTO(petList, employeeList, date, skillSet));
//        ScheduleDTO scheduleDTO = scheduleController.getAllSchedules().get(0);
//
//        Assertions.assertEquals(scheduleDTO.getActivities(), skillSet);
//        Assertions.assertEquals(scheduleDTO.getDate(), date);
//        Assertions.assertEquals(scheduleDTO.getEmployeeIds(), employeeList);
//        Assertions.assertEquals(scheduleDTO.getPetIds(), petList);
//    }
//
//    @Test
//    public void testFindScheduleByEntities() {
//        ScheduleDTO sched1 = populateSchedule(1, 2, LocalDate.of(2019, 12, 25), Sets.newHashSet(SkillEnum.FEEDING, SkillEnum.WALKING));
//        ScheduleDTO sched2 = populateSchedule(3, 1, LocalDate.of(2019, 12, 26), Sets.newHashSet(SkillEnum.PETTING));
//
//        //add a third schedule that shares some employees and pets with the other schedules
//        ScheduleDTO sched3 = new ScheduleDTO();
//        sched3.setEmployeeIds(sched1.getEmployeeIds());
//        sched3.setPetIds(sched2.getPetIds());
//        sched3.setActivities(Sets.newHashSet(SkillEnum.SHAVING, SkillEnum.PETTING));
//        sched3.setDate(LocalDate.of(2020, 3, 23));
//        scheduleController.createSchedule(sched3);
//
//        /*
//            We now have 3 schedule entries. The third schedule entry has the same employees as the 1st schedule
//            and the same pets/owners as the second schedule. So if we look up schedule entries for the employee from
//            schedule 1, we should get both the first and third schedule as our result.
//         */
//
//        //Employee 1 in is both schedule 1 and 3
//        List<ScheduleDTO> scheds1e = scheduleController.getScheduleForEmployee(sched1.getEmployeeIds().get(0));
//        compareSchedules(sched1, scheds1e.get(0));
//        compareSchedules(sched3, scheds1e.get(1));
//
//        //Employee 2 is only in schedule 2
//        List<ScheduleDTO> scheds2e = scheduleController.getScheduleForEmployee(sched2.getEmployeeIds().get(0));
//        compareSchedules(sched2, scheds2e.get(0));
//
//        //Pet 1 is only in schedule 1
//        List<ScheduleDTO> scheds1p = scheduleController.getScheduleForPet(sched1.getPetIds().get(0));
//        compareSchedules(sched1, scheds1p.get(0));
//
//        //Pet from schedule 2 is in both schedules 2 and 3
//        List<ScheduleDTO> scheds2p = scheduleController.getScheduleForPet(sched2.getPetIds().get(0));
//        compareSchedules(sched2, scheds2p.get(0));
//        compareSchedules(sched3, scheds2p.get(1));
//
//        //Owner of the first pet will only be in schedule 1
//        List<ScheduleDTO> scheds1c = scheduleController.getScheduleForCustomer(userController.getOwnerByPet(sched1.getPetIds().get(0)).getId());
//        compareSchedules(sched1, scheds1c.get(0));
//
//        //Owner of pet from schedule 2 will be in both schedules 2 and 3
//        List<ScheduleDTO> scheds2c = scheduleController.getScheduleForCustomer(userController.getOwnerByPet(sched2.getPetIds().get(0)).getId());
//        compareSchedules(sched2, scheds2c.get(0));
//        compareSchedules(sched3, scheds2c.get(1));
//    }
//
//
//    private static EmployeeDTO createEmployeeDTO() {
//        EmployeeDTO employeeDTO = new EmployeeDTO();
//
//        UUID id = UUID.randomUUID();
//
//        employeeDTO.setUsername("testem_"+id);
//        employeeDTO.setName("TestEmployee_"+id);
//        employeeDTO.setSkills(Sets.newHashSet(SkillEnum.FEEDING, SkillEnum.PETTING));
//        return employeeDTO;
//    }

//    private static CustomerDTO createCustomerDTO() {
//        CustomerDTO customerDTO = new CustomerDTO();
//
//        UUID id = UUID.randomUUID();
//
//        customerDTO.setUsername("testctm_"+id);
//        customerDTO.setName("TestCustomer_"+id);
//        customerDTO.setPhoneNumber("123-456-789");
//        return customerDTO;
//    }

    private static String getCreateEmployeeRequestBody (String username, String name, List<String> skills ,List<String> daysAvailable){
        return "{\n" +
                "    \"username\":" + "\"" + username + "\"" + ",\n" +
                "    \"name\":" + "\"" + name + "\"" + ",\n" +
                "    \"skills\":" + skills + ",\n" +
                "    \"daysAvailable\":" + daysAvailable + "\n" +
                "}";
    }

    private static String getCreateCustomerRequestBody (String username, String name, String phoneNumber){
        return "{\n" +
                "    \"username\":" + "\"" + username + "\"" + ",\n" +
                "    \"name\":" + "\"" + name + "\"" + ",\n" +
                "    \"phoneNumber\":" + "\"" + phoneNumber + "\"" + "\n" +
                "}";
    }

    private static String getCreateCustomerRequestBody (String username, String name, String phoneNumber, List<Integer> petIds){
        return "{\n" +
                "    \"username\":" + "\"" + username + "\"" + ",\n" +
                "    \"name\":" + "\"" + name + "\"" + ",\n" +
                "    \"phoneNumber\":" + "\"" + phoneNumber + "\"" + ",\n" +
                "    \"petIds\":" + petIds + ",\n" +
                "}";
    }

    private static String getCreatePetRequestBody (String type, String name, Long ownerId, String birthDate){
        return "{\n" +
                "    \"type\":" + "\"" + type + "\"" + ",\n" +
                "    \"name\":" + "\"" + name + "\"" + ",\n" +
                "    \"ownerId\":" + ownerId + ",\n" +
                "    \"birthDate\":" + "\"" + birthDate + "\"" + "\n" +
                "}";
    }

//
//    private static PetDTO createPetDTO() {
//        PetDTO petDTO = new PetDTO();
//        petDTO.setName("TestPet");
//        petDTO.setType(PetType.CAT);
//        petDTO.setBirthDate(LocalDate.of(2018,10,5));
//        return petDTO;
//    }
//
//    private static EmployeeRequestDTO createEmployeeRequestDTO() {
//        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
//        employeeRequestDTO.setDate(LocalDate.of(2019, 12, 25));
//        employeeRequestDTO.setSkills(Sets.newHashSet(SkillEnum.FEEDING, SkillEnum.WALKING));
//        return employeeRequestDTO;
//    }
//
//    private static ScheduleDTO createScheduleDTO(List<Long> petIds, List<Long> employeeIds, LocalDate date, Set<SkillEnum> activities) {
//        ScheduleDTO scheduleDTO = new ScheduleDTO();
//        scheduleDTO.setPetIds(petIds);
//        scheduleDTO.setEmployeeIds(employeeIds);
//        scheduleDTO.setDate(date);
//        scheduleDTO.setActivities(activities);
//        return scheduleDTO;
//    }
//
//    private ScheduleDTO populateSchedule(int numEmployees, int numPets, LocalDate date, Set<SkillEnum> activities) {
//        List<Long> employeeIds = IntStream.range(0, numEmployees)
//                .mapToObj(i -> createEmployeeDTO())
//                .map(e -> {
//                    e.setSkills(activities);
//                    e.setDaysAvailable(Sets.newHashSet(date.getDayOfWeek()));
//                    return userController.saveEmployee(e).getId();
//                }).collect(Collectors.toList());
//        CustomerDTO cust = userController.saveCustomer(createCustomerDTO());
//        List<Long> petIds = IntStream.range(0, numPets)
//                .mapToObj(i -> createPetDTO())
//                .map(p -> {
//                    p.setOwnerId(cust.getId());
//                    return petController.savePet(p).getId();
//                }).collect(Collectors.toList());
//        return scheduleController.createSchedule(createScheduleDTO(petIds, employeeIds, date, activities));
//    }
//
//    private static void compareSchedules(ScheduleDTO sched1, ScheduleDTO sched2) {
//        Assertions.assertEquals(sched1.getPetIds(), sched2.getPetIds());
//        Assertions.assertEquals(sched1.getActivities(), sched2.getActivities());
//        Assertions.assertEquals(sched1.getEmployeeIds(), sched2.getEmployeeIds());
//        Assertions.assertEquals(sched1.getDate(), sched2.getDate());
//    }

}
