package ru.gasevsky.jarsoft;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gasevsky.jarsoft.dto.BannerDto;
import ru.gasevsky.jarsoft.model.Banner;
import ru.gasevsky.jarsoft.model.Category;

import java.util.List;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Slf4j
public class ControllersTests extends ApplicationTest {
    private ObjectMapper objectMapper;
    private ObjectWriter ow;
    private final String catUrl = "/category/";
    private final String bannerUrl = "/banner/";

    private final List<Category> categories;
    private final List<BannerDto> banners;

    {
        Category cat1 = new Category();
        cat1.setId(0);
        cat1.setName("CAT1");
        cat1.setReqName("REQ1");
        cat1.setDeleted(false);
        Category cat2 = new Category();
        cat2.setId(0);
        cat2.setName("CAT2");
        cat2.setReqName("REQ2");
        cat2.setDeleted(false);
        Category cat3 = new Category();
        cat3.setId(0);
        cat3.setName("категория1");
        cat3.setReqName("запрос1");
        cat3.setDeleted(false);
        Category cat4 = new Category();
        cat4.setId(0);
        cat4.setName("категория2");
        cat4.setReqName("запрос2");
        cat4.setDeleted(false);
        this.categories = List.of(cat1, cat2, cat3, cat4);

        BannerDto banner1 = new BannerDto();
        banner1.setId(0);
        banner1.setName("BANNER1");
        banner1.setCategory(cat1.getId());
        banner1.setPrice(100.50F);
        banner1.setContent("CONTENT1");
        banner1.setDeleted(false);
        BannerDto banner2 = new BannerDto();
        banner2.setId(0);
        banner2.setName("BANNER2");
        banner2.setCategory(cat2.getId());
        banner2.setPrice(200.50F);
        banner2.setContent("CONTENT2");
        banner2.setDeleted(false);
        BannerDto banner3 = new BannerDto();
        banner3.setId(0);
        banner3.setName("BANNER3");
        banner3.setCategory(cat1.getId());
        banner3.setPrice(300.50F);
        banner3.setContent("CONTENT3");
        banner3.setDeleted(false);
        BannerDto banner4 = new BannerDto();
        banner4.setId(0);
        banner4.setName("BANNER4");
        banner4.setCategory(cat1.getId());
        banner4.setPrice(400.50F);
        banner4.setContent("CONTENT4");
        banner4.setDeleted(false);
        banners = List.of(banner1, banner2, banner3, banner4);
    }


    @Autowired
    public void setModelMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.ow = objectMapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void loadBannersWhenEmpty() throws Exception {
        try {
            this.mockMvc.perform(get(bannerUrl))
                    .andDo(mvcResult -> {
                        log.info("TEST METHOD");
                        log.info("Response status: " + mvcResult.getResponse().getStatus());
                        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
                    })
                    .andDo(print())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(0)))
            ;
            throw new RuntimeException("rollback for accept method");
        } catch (RuntimeException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    @Transactional()
    public void loadBannerAfterInserted() throws Exception {
        String requestJson = ow.writeValueAsString(banners.get(0));
        try {
            this.mockMvc.perform(post(bannerUrl)
                    .contentType(APPLICATION_JSON).content(requestJson))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasKey("id")))
                    .andExpect(jsonPath("$", hasKey("name")))
                    .andExpect(jsonPath("$.name", Matchers.is(banners.get(0).getName())))
                    .andExpect(jsonPath("$", hasKey("price")))
                    .andExpect(jsonPath("$.price", Matchers.is(banners.get(0).getPrice().doubleValue())))
                    .andExpect(jsonPath("$", hasKey("category")))
                    .andExpect(jsonPath("$.category", Matchers.is(banners.get(0).getCategory())))
                    .andExpect(jsonPath("$", hasKey("content")))
                    .andExpect(jsonPath("$.content", Matchers.is(banners.get(0).getContent())))
                    .andExpect(jsonPath("$", hasKey("deleted")))
                    .andExpect(jsonPath("$.deleted", Matchers.is(banners.get(0).getDeleted())))
            ;
            throw new TestRollBackException("rollback for accept method");
        } catch (TestRollBackException e) {
            log.info(e.getMessage());
        }
    }

    @Test
//    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void whenRepeatingKeysThanBannerNotInsertedWith409Status() throws Exception {
        String requestJson = ow.writeValueAsString(banners.get(0));
//        try {
            this.mockMvc.perform(post(bannerUrl)
                    .contentType(APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isCreated())
            ;
//            this.mockMvc.perform(post(bannerUrl)
//                    .contentType(APPLICATION_JSON).content(requestJson))
////                    .andExpect(status().isConflict())
////                    .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
//            ;
//            throw new RuntimeException("rollback for accept method");
//        } catch (RuntimeException e) {
//            log.info(e.getMessage());
//        }
    }

    @Test
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void loadCategoriesWhenEmpty() throws Exception {
        try {
            this.mockMvc.perform(get(catUrl))
                    .andDo(mvcResult -> {
                        log.info("TEST METHOD");
                        log.info("Response status: " + mvcResult.getResponse().getStatus());
                        Assert.assertEquals(200, mvcResult.getResponse().getStatus());
                    })
                    .andDo(print())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(0)))
            ;
            throw new RuntimeException("rollback for accept method");
        } catch (RuntimeException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void loadCategoryAfterInserted() throws Exception {
        String requestJson = ow.writeValueAsString(categories.get(0));
        try {
            this.mockMvc.perform(post(catUrl)
                    .contentType(APPLICATION_JSON).content(requestJson))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasKey("id")))
                    .andExpect(jsonPath("$", hasKey("name")))
                    .andExpect(jsonPath("$.name", Matchers.is(categories.get(0).getName())))
                    .andExpect(jsonPath("$", hasKey("reqName")))
                    .andExpect(jsonPath("$.reqName", Matchers.is(categories.get(0).getReqName())))
                    .andExpect(jsonPath("$", hasKey("deleted")))
                    .andExpect(jsonPath("$.deleted", Matchers.is(categories.get(0).getDeleted())))
            ;
            throw new RuntimeException("rollback for accept method");
        } catch (RuntimeException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void whenRepeatingKeysThanCategoryNotInsertedWith409Status() throws Exception {
        String requestJson = ow.writeValueAsString(categories.get(0));
        try {
            this.mockMvc.perform(post(catUrl)
                    .contentType(APPLICATION_JSON).content(requestJson))
                    .andDo(print())
                    .andExpect(status().isCreated())
            ;
            this.mockMvc.perform(post(catUrl)
                    .contentType(APPLICATION_JSON).content(requestJson))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
            ;
            throw new RuntimeException("rollback for accept method");
        } catch (RuntimeException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void whenSearchCategoriesByPartOfNameThanGetEm() throws Exception {
        try {
            for (Category category : categories) {
                this.mockMvc.perform(post(catUrl)
                        .contentType(APPLICATION_JSON).content(ow.writeValueAsString(category)))
                        .andDo(print())
                        .andExpect(status().isCreated())
                ;
            }
            this.mockMvc.perform(get(catUrl + "search/cat")
                    .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(content().contentType(APPLICATION_JSON))
            ;
            throw new RuntimeException("rollback for accept method");
        } catch (RuntimeException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void whenUpdateCategoryThanLoadIt() throws Exception {
        try {
            this.mockMvc.perform(post(catUrl)
                    .contentType(APPLICATION_JSON).content(ow.writeValueAsString(categories.get(1))));
            String jsonBefore = ow.writeValueAsString(categories.get(0));
            final Category[] res1 = new Category[1];
            this.mockMvc.perform(post(catUrl)
                    .contentType(APPLICATION_JSON).content(jsonBefore))
                    .andDo(mvcResult -> {
                        res1[0] = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Category.class);
                    })
                    .andExpect(status().isCreated())
            ;
            Assert.assertEquals(categories.get(0).getName(), res1[0].getName());
            Assert.assertEquals(categories.get(0).getReqName(), res1[0].getReqName());
            Assert.assertEquals(categories.get(0).getDeleted(), res1[0].getDeleted());

            res1[0].setName("UPDATED");
            res1[0].setReqName("REQUP");

            String jsonAfter = ow.writeValueAsString(res1[0]);
            this.mockMvc.perform(put(catUrl)
                    .contentType(APPLICATION_JSON).content(jsonAfter))
                    .andExpect(status().isOk())
            ;
            this.mockMvc.perform(get(catUrl + "search/updated")
                    .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(content().contentType(APPLICATION_JSON))
            ;
            throw new RuntimeException("rollback for accept method");
        } catch (RuntimeException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    public void whenDeleteCategoryThanItsStatusIsDeleted() throws Exception {
        try {
            this.mockMvc.perform(post(catUrl)
                    .contentType(APPLICATION_JSON).content(ow.writeValueAsString(categories.get(1))));
            String jsonBefore = ow.writeValueAsString(categories.get(0));
            final Category[] res1 = new Category[1];
            this.mockMvc.perform(post(catUrl)
                    .contentType(APPLICATION_JSON).content(jsonBefore))
                    .andDo(mvcResult -> {
                        res1[0] = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Category.class);
                    })
                    .andExpect(status().isCreated())
            ;
            Assert.assertEquals(categories.get(0).getName(), res1[0].getName());
            Assert.assertEquals(categories.get(0).getReqName(), res1[0].getReqName());
            Assert.assertEquals(categories.get(0).getDeleted(), res1[0].getDeleted());

            String url = catUrl + res1[0].getId();

            this.mockMvc.perform(get(catUrl + "search/cat1")
                    .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(content().contentType(APPLICATION_JSON))
            ;

            this.mockMvc.perform(delete(url)
                    .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
            ;
            this.mockMvc.perform(get(catUrl + "search/cat1")
                    .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)))
                    .andExpect(content().contentType(APPLICATION_JSON))
            ;
            throw new RuntimeException("rollback for accept method");
        } catch (RuntimeException e) {
            log.info(e.getMessage());
        }
    }

//    @Test
//    public void adminPageTest() throws Exception {
//        this.mockMvc.perform(get("/admin"))
//                .andDo(print())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().string(containsString("test order")));
//    }
//
//    @Test
//    @WithUserDetails("admin")
//    public void loadOrdersWithDateRangeOnAdminPage() throws Exception {
//        String from = LocalDateTime.of(2019, 5, 1, 12, 0, 0).toString();
//        String to = LocalDateTime.of(2019, 7, 1, 12, 0, 0).toString();
//        this.mockMvc.perform(get("/admin").param("from", from).param("to", to))
//                .andDo(mvcResult -> {
//                    System.out.println("TEST METHOD");
//                    System.out.println(mvcResult.getResponse().getContentAsString());
//                })
//                .andDo(print())
//                .andExpect(content().contentType(APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].id", Matchers.is(5)))
//                .andExpect(jsonPath("$[1].id", Matchers.is(6)))
//                .andExpect(jsonPath("$[2].id", Matchers.is(7)))
//                .andExpect(jsonPath("$[0].name", Matchers.is("test order 5")))
//                .andExpect(jsonPath("$[1].name", Matchers.is("test order 6")))
//                .andExpect(jsonPath("$[2].name", Matchers.is("test order 7")))
//                .andExpect(jsonPath("$[0]", hasKey("created")))
//                .andExpect(jsonPath("$[0].created", Matchers.is("2019-05-01 12:00:00")))
//
//                .andExpect(jsonPath("$[1]", hasKey("created")))
//                .andExpect(jsonPath("$[1].created", Matchers.is("2019-06-01 12:00:00")))
//
//                .andExpect(jsonPath("$[2]", hasKey("created")))
//                .andExpect(jsonPath("$[2].created", Matchers.is("2019-07-01 12:00:00")))
//
//                .andExpect(jsonPath("$[0].holder", hasKey("roles")))
//                .andExpect(jsonPath("$[0].holder.roles", Matchers.contains("ROLE_OPERATOR")))
//                .andExpect(jsonPath("$[1].holder", hasKey("roles")))
//                .andExpect(jsonPath("$[1].holder.roles", Matchers.contains("ROLE_OPERATOR")))
//                .andExpect(jsonPath("$[2].holder", hasKey("roles")))
//                .andExpect(jsonPath("$[2].holder.roles", Matchers.contains("ROLE_OPERATOR")))
//        ;
//    }
//
//    @Test
//    @WithUserDetails("operator1")
//    public void loadOrdersWithDateRangeOnOperatorsPage() throws Exception {
//        String from = LocalDateTime.of(2019, 5, 1, 12, 0, 0).toString();
//        String to = LocalDateTime.of(2019, 7, 1, 12, 0, 0).toString();
//        this.mockMvc.perform(get("/order-manage").param("from", from).param("to", to))
//                .andDo(print())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].id", Matchers.is(5)))
//                .andExpect(jsonPath("$[1].id", Matchers.is(6)))
//                .andExpect(jsonPath("$[2].id", Matchers.is(7)))
//                .andExpect(jsonPath("$[0].name", Matchers.is("test order 5")))
//                .andExpect(jsonPath("$[1].name", Matchers.is("test order 6")))
//                .andExpect(jsonPath("$[2].name", Matchers.is("test order 7")))
//                .andExpect(jsonPath("$[0]", hasKey("created")))
//                .andExpect(jsonPath("$[0].created", Matchers.is("2019-05-01 12:00:00")))
//                .andExpect(jsonPath("$[1]", hasKey("created")))
//                .andExpect(jsonPath("$[1].created", Matchers.is("2019-06-01 12:00:00")))
//                .andExpect(jsonPath("$[2]", hasKey("created")))
//                .andExpect(jsonPath("$[2].created", Matchers.is("2019-07-01 12:00:00")))
//
//                .andExpect(jsonPath("$[0]", hasKey("holderName")))
//                .andExpect(jsonPath("$[1]", hasKey("holderName")))
//                .andExpect(jsonPath("$[2]", hasKey("holderName")))
//                .andExpect(jsonPath("$[0].holderName", Matchers.is("operator1")))
//                .andExpect(jsonPath("$[1].holderName", Matchers.is("operator1")))
//                .andExpect(jsonPath("$[2].holderName", Matchers.is("operator1")))
//
//        ;
//    }
//
//    @Test
//    @WithUserDetails("courier1")
//    public void loadOrdersWithDateRangeOnCouriersPage() throws Exception {
//        String from = LocalDateTime.of(2019, 5, 1, 12, 0, 0).toString();
//        String to = LocalDateTime.of(2019, 7, 1, 12, 0, 0).toString();
//        this.mockMvc.perform(get("/order").param("from", from).param("to", to))
//                .andDo(print())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[0].id", Matchers.is(5)))
//                .andExpect(jsonPath("$[1].id", Matchers.is(6)))
//                .andExpect(jsonPath("$[2].id", Matchers.is(7)))
//                .andExpect(jsonPath("$[0].name", Matchers.is("test order 5")))
//                .andExpect(jsonPath("$[1].name", Matchers.is("test order 6")))
//                .andExpect(jsonPath("$[2].name", Matchers.is("test order 7")))
//                .andExpect(jsonPath("$[0]", Matchers.not(hasKey("created"))))
//                .andExpect(jsonPath("$[1]", Matchers.not(hasKey("created"))))
//                .andExpect(jsonPath("$[2]", Matchers.not(hasKey("created"))))
//                .andExpect(jsonPath("$[0].holderName", Matchers.is("operator1")))
//                .andExpect(jsonPath("$[1].holderName", Matchers.is("operator1")))
//                .andExpect(jsonPath("$[2].holderName", Matchers.is("operator1")))
//        ;
//    }
//
//    /**
//     * Courier accepts the order.
//     * <p>
//     * Before courier accepts the order, orders holder is operator.
//     * After courier accepts the order,  the courier becomes the holder of the order.
//     */
//    @Test
//    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
//    public void whenCourierAcceptsOrderThenOrderChanges() throws Exception {
//        try {
//            this.mockMvc.perform(get("/order"))
//                    .andDo(print())
//                    .andExpect(jsonPath("$[0].id", Matchers.is(1)))
//                    .andExpect(jsonPath("$[%d].holderId", Matchers.is(2)))
//            ;
//
//            this.mockMvc.perform(get("/order/1"))
//                    .andDo(print())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$", hasKey("id")))
//                    .andExpect(jsonPath("$.id", Matchers.is(1)))
//                    .andExpect(jsonPath("$", hasKey("holderId")))
//                    .andExpect(jsonPath("$.holderId", Matchers.is(3)))
//            ;
//            throw new RuntimeException("rollback for accept method");
//        } catch (RuntimeException e) {
//            log.info(e.getMessage());
//        }
//    }
//
//    /**
//     * Courier completes the order.
//     * <p>
//     * Before courier completes the order, the length of list of orders that are not completed is 11.
//     * After courier completes the order,  the length of list of orders that are not completed is 10.
//     */
//    @Test
//    @WithUserDetails("courier1")
//    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
//    public void whenCourierCompleteOrderThenListOfOrdersGetShorterByOne() throws Exception {
//        try {
//            this.mockMvc.perform(get("/order"))
//                    .andDo(print())
//                    .andExpect(jsonPath("$", hasSize(12)));
//
//            this.mockMvc.perform(get("/order/1"))
//                    .andDo(print())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$", hasKey("id")))
//                    .andExpect(jsonPath("$.id", Matchers.is(1)))
//                    .andExpect(jsonPath("$", hasKey("holderName")))
//                    .andExpect(jsonPath("$.holderName", Matchers.is("courier1")))
//                    .andExpect(jsonPath("$", hasKey("holderTelephone")))
//                    .andExpect(jsonPath("$.holderTelephone", Matchers.is("2")));
//
//            this.mockMvc.perform(delete("/order/1"))
//                    .andDo(print());
//
//            this.mockMvc.perform(get("/order"))
//                    .andDo(print())
//                    .andExpect(jsonPath("$", hasSize(11)));
//
//            throw new RuntimeException("rollback for complete method");
//        } catch (RuntimeException e) {
//            log.info(e.getMessage());
//        }
//
//    }
//
//
//    /**
//     * Courier cancels the order.
//     * <p>
//     * Before courier cancels the order, the length of list of orders to call is 1;
//     * After courier cancels the order,  the length of list of orders to call is 2;
//     */
//    @Autowired
//    private UserService userService;
//
//
//    @Test
//    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
//    public void whenCourierCancelOrderThenListOfOrdersToCallContainsThisOrder() throws Exception {
//        try {
//            this.mockMvc.perform(get("/order-manage/toCall")
//                    .with(user(userService.loadUserByUsername("operator1"))))
//                    .andDo(print())
//                    .andExpect(jsonPath("$", hasSize(0)));
//
//            this.mockMvc.perform(get("/order/1")
//                    .with(user(userService.loadUserByUsername("courier1"))))
//                    .andDo(print())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$", hasKey("id")))
//                    .andExpect(jsonPath("$", hasKey("holderName")))
//                    .andExpect(jsonPath("$.holderName", Matchers.is("courier1")))
//            ;
//
//            this.mockMvc.perform(post("/order/1")
//                    .with(user(userService.loadUserByUsername("courier1"))))
//                    .andDo(print());
//
//            this.mockMvc.perform(get("/order-manage/toCall")
//                    .with(user(userService.loadUserByUsername("operator1"))))
//                    .andDo(print())
//                    .andExpect(jsonPath("$[0].holderName", Matchers.nullValue()))
//                    .andExpect(jsonPath("$[0].holderTelephone", Matchers.nullValue()))
//                    .andExpect(jsonPath("$[0].id", Matchers.is(1)));
//            throw new RuntimeException("rollback for cancel method");
//        } catch (RuntimeException e) {
//            log.info(e.getMessage());
//        }
//    }
//
//    /**
//     * Operator add new order.
//     * <p>
//     * Before operator add the order, the length of list of orders is a.
//     * After operator add the order,  the length of list of orders is a+1.
//     * And list contains this order.
//     */
//    @Test
//    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
//    public void whenOperatorAddOrderThenListOfOrdersGetLongerByOne() throws Exception {
//        try {
//            this.mockMvc.perform(get("/order-manage")
//                    .with(user(userService.loadUserByUsername("operator1"))))
//                    .andDo(print())
//                    .andExpect(jsonPath("$", hasSize(12)));
//            String name = "new test order";
//            String desc = "new test order desc";
//            String cliName = "new test order cli_name";
//            String cliTel = "new test order cli_tel";
//
//            OrderDtoCourier orderDto = new OrderDtoCourier();
//            orderDto.setName(name);
//            orderDto.setDescription(desc);
//            orderDto.setClientName(cliName);
//            orderDto.setClientTelephone(cliTel);
//
//            ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
//            String order = ow.writeValueAsString(orderDto);
//
//            this.mockMvc.perform(post("/order-manage")
//                    .contentType(APPLICATION_JSON)
//                    .content(order)
//                    .with(user(userService.loadUserByUsername("operator1"))))
//                    .andDo(print());
//
//            this.mockMvc.perform(get("/order-manage")
//                    .with(user(userService.loadUserByUsername("operator1"))))
//                    .andDo(print())
//                    .andExpect(jsonPath("$", hasSize(13)))
//                    .andExpect(jsonPath("$[12].name", Matchers.is(name)))
//                    .andExpect(jsonPath("$[12].description", Matchers.is(desc)))
//                    .andExpect(jsonPath("$[12].clientName", Matchers.is(cliName)))
//                    .andExpect(jsonPath("$[12].clientTelephone", Matchers.is(cliTel)))
//
//            ;
//
//            throw new RuntimeException("rollback for addNewOrder method");
//        } catch (RuntimeException e) {
//            log.info(e.getMessage());
//        }
//
//  }

}

