<template>
  <div class="indexPage">
    <a-input-search
      v-model:value="searchParams.text"
      placeholder="input search text"
      enter-button="Search"
      size="large"
      @search="onSearch"
    />
    <MySeparator />
    <a-tabs v-model:activeKey="activeKey" @change="onTabChange">
      <a-tab-pane key="Article" tab="文章">
        <ArticlePage :postList="postList" />
      </a-tab-pane>
      <a-tab-pane key="Picture" tab="图片">
        <PictureList />
      </a-tab-pane>
      <a-tab-pane key="User" tab="用户">
        <UserList :userList="userList" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup lang="ts">
import { watchEffect, ref } from "vue";
import ArticlePage from "@/components/ArticleList.vue";
import UserList from "@/components/UserList.vue";
import PictureList from "@/components/PictureList.vue";
import MySeparator from "@/components/MySeparator.vue";
import { useRoute, useRouter } from "vue-router";
import MyAxios from "@/plugins/MyAxios";

const postList = ref([]);
const userList = ref([]);
const router = useRouter();
const route = useRoute();
const activeKey = route.params.category;
const initSearchParams = {
  text: "",
  pageSize: 10,
  pageNumber: 1,
};
const searchParams = ref(initSearchParams);
MyAxios.post("/post/list/page/vo", {
  params: {
    id: "1691380908365463553",
  },
}).then((res: any) => {
  console.log(res);
  postList.value = res.records;
});
MyAxios.post("/user/list/page/vo", {
  // params: {
  //   id: "1691380908365463553",
  // },
}).then((res: any) => {
  console.log(res);
  userList.value = res.records;
});
watchEffect(() => {
  searchParams.value = {
    ...initSearchParams,
    text: route.query.text as string,
  };
});
const onTabChange = (key: string) => {
  router.push({
    path: `${key}`,
    query: {
      text: searchParams.value.text,
    },
  });
};

const onSearch = () => {
  router.push({
    query: {
      text: searchParams.value.text,
    },
  });
};
</script>
