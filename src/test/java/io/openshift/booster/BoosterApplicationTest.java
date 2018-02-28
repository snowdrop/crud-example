package io.openshift.booster;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.openshift.booster.service.Fruit;
import io.openshift.booster.service.FruitController;
import io.openshift.booster.service.FruitRepository;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(FruitController.class)
public class BoosterApplicationTest {

    private static final String PATH = "/api/fruits";
    private static final String TEMPLATE_PATH_WITH_ID = PATH + "/{id}";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FruitRepository fruitRepository;

    @Test
    public void testGetAll() throws Exception {
        given(fruitRepository.findAll())
                .willReturn(Lists.newArrayList(new Fruit("Cherry"), new Fruit("Apple")));

        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Cherry", "Apple")));
    }

    @Test
    public void testGetEmptyArray() throws Exception {
        given(fruitRepository.findAll())
                .willReturn(Lists.newArrayList());

        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        verify(fruitRepository, times(1)).findAll();
    }

    @Test
    public void testGetOne() throws Exception {
        final int id = 1;
        given(fruitRepository.findOne(id))
                .willReturn(new Fruit("Cherry"));
        given(fruitRepository.exists(id)).willReturn(true);

        mvc.perform(get(TEMPLATE_PATH_WITH_ID, id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Cherry")));

        verify(fruitRepository, times(1)).findOne(id);
        verify(fruitRepository, times(1)).exists(id);
    }

    @Test
    public void testGetNotExisting() throws Exception {
        final int id = 0;
        given(fruitRepository.exists(id)).willReturn(false);

        mvc.perform(get(TEMPLATE_PATH_WITH_ID, id))
                .andExpect(status().isNotFound());

        verify(fruitRepository, times(1)).exists(id);
    }

    @Test
    public void testPost() throws Exception {
        final Fruit fruit = new Fruit("Cherry");
        final Fruit fruitWithId = new Fruit(fruit.getName());
        fruitWithId.setId(1);

        given(fruitRepository.save(Mockito.any(Fruit.class))).willReturn(fruitWithId);

        mvc.perform(
                post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fruit))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is(fruitWithId.getName())))
        .andExpect(jsonPath("$.id", is(fruitWithId.getId())));

        verify(fruitRepository, times(1)).save(Mockito.any(Fruit.class));
    }

    @Test
    public void testPostWithWrongPayload() throws Exception {
        final Fruit fruit = new Fruit("Cherry");
        final Fruit fruitWithId = new Fruit(fruit.getName());
        fruitWithId.setId(1);

        mvc.perform(
                post(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruitWithId))
        )
        .andExpect(status().isUnprocessableEntity());

        verify(fruitRepository, never()).save(Mockito.any(Fruit.class));
    }

    @Test
    public void testPostWithNonJsonPayload() throws Exception {
        mvc.perform(post(PATH).contentType(MediaType.APPLICATION_XML))
           .andExpect(status().isUnsupportedMediaType());

        verify(fruitRepository, never()).save(Mockito.any(Fruit.class));
    }

    @Test
    public void testPostWithEmptyPayload() throws Exception {
        mvc.perform(post(PATH).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnsupportedMediaType());

        verify(fruitRepository, never()).save(Mockito.any(Fruit.class));
    }

    @Test
    public void testPut() throws Exception {
        final Fruit fruit = new Fruit("Cherry");
        final Fruit fruitWithId = new Fruit(fruit.getName());
        fruitWithId.setId(1);

        given(fruitRepository.exists(fruitWithId.getId())).willReturn(true);
        given(fruitRepository.save(Mockito.any(Fruit.class))).willReturn(fruitWithId);

        mvc.perform(
                put(TEMPLATE_PATH_WITH_ID, fruitWithId.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruit))
        )
        .andExpect(status().isOk());

        verify(fruitRepository, times(1)).exists(fruitWithId.getId());
        verify(fruitRepository, times(1)).save(Mockito.any(Fruit.class));
    }

    @Test
    public void testPutNotExisting() throws Exception {
        final Fruit fruit = new Fruit("Cherry");
        final Fruit fruitWithId = new Fruit(fruit.getName());
        fruitWithId.setId(1);

        given(fruitRepository.exists(fruitWithId.getId())).willReturn(false);

        mvc.perform(
                put(TEMPLATE_PATH_WITH_ID, fruitWithId.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruit))
        )
        .andExpect(status().isNotFound());

        verify(fruitRepository, times(1)).exists(fruitWithId.getId());
        verify(fruitRepository, never()).save(Mockito.any(Fruit.class));
    }

    @Test
    public void testPutWithWrongPayload() throws Exception {
        final Fruit fruit = new Fruit("Cherry");
        final Fruit fruitWithId = new Fruit(fruit.getName());
        fruitWithId.setId(1);

        given(fruitRepository.exists(fruitWithId.getId())).willReturn(true);

        mvc.perform(
                put(TEMPLATE_PATH_WITH_ID, fruitWithId.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fruitWithId))
        )
        .andExpect(status().isUnprocessableEntity());

        verify(fruitRepository, times(1)).exists(fruitWithId.getId());
        verify(fruitRepository, never()).save(Mockito.any(Fruit.class));
    }

    @Test
    public void testPutWithNonJsonPayload() throws Exception {
        mvc.perform(put(TEMPLATE_PATH_WITH_ID, 0).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isUnsupportedMediaType());

        verify(fruitRepository, never()).save(Mockito.any(Fruit.class));
    }

    @Test
    public void testPutWithEmptyPayload() throws Exception{
        final int id = 0;

        given(fruitRepository.exists(id)).willReturn(true);

        mvc.perform(put(TEMPLATE_PATH_WITH_ID, id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnsupportedMediaType());

        verify(fruitRepository, times(1)).exists(id);
        verify(fruitRepository, never()).save(Mockito.any(Fruit.class));
    }

    @Test
    public void testDelete() throws Exception {
        final int id = 0;

        given(fruitRepository.exists(id)).willReturn(true);

        mvc.perform(delete(TEMPLATE_PATH_WITH_ID, id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(fruitRepository, times(1)).exists(id);
        verify(fruitRepository, times(1)).delete(id);
    }

    @Test
    public void testDeleteNotExisting() throws Exception {
        final int id = 0;

        given(fruitRepository.exists(id)).willReturn(false);

        mvc.perform(delete(TEMPLATE_PATH_WITH_ID, id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(fruitRepository, times(1)).exists(id);
        verify(fruitRepository, never()).delete(id);
    }
}
