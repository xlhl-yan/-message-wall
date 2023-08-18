<template>
  <div class="indexPage">
    <a-input-search
      v-model:value="searchText"
      placeholder="请输入关键字"
      enter-button="搜索"
      size="large"
      @search="onSearch"
    />
    <MySeparator />
    <a-tabs v-model:activeKey="activeKey" @change="onTabChange">
      <a-tab-pane key="Article" tab="文章">
        <PostList :postList="postList" />
      </a-tab-pane>
      <a-tab-pane key="Picture" tab="图片">
        <PictureList :pictureList="pictureList" />
      </a-tab-pane>
      <a-tab-pane key="User" tab="用户">
        <UserList :userList="userList" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup lang="ts">
import { watchEffect, ref } from "vue";
import PostList from "@/components/PostList.vue";
import UserList from "@/components/UserList.vue";
import PictureList from "@/components/PictureList.vue";
import MySeparator from "@/components/MySeparator.vue";
import { useRoute, useRouter } from "vue-router";
import MyAxios from "@/plugins/MyAxios";
import { message } from "ant-design-vue";

const postList = ref([]);
const userList = ref([]);
const pictureList = ref([]);
const router = useRouter();
const route = useRoute();
const activeKey = route.params.category;
const initSearchParams = {
  text: "",
  pageSize: 10,
  pageNumber: 1,
  type: activeKey,
};

const onSearch = (value: string) => {
  router.push({
    query: {
      ...searchParams.value,
      text: value,
    },
  });
};
const searchText = ref(route.query.text || "");

const searchParams = ref(initSearchParams);

/**
 * 获取全部数据
 * @param params
 */
const loadDataAll = (params: any) => {
  const query = {
    ...params,
    searchText: params.text,
    type: "All",
  };
  MyAxios.post("/search/all", query).then((res: any) => {
    postList.value = res.postList;
    userList.value = res.userList;
    pictureList.value = res.pictureList;
  });
};

/**
 * 获取单一数据源
 * @param params
 */
const loadData = (params: any) => {
  const { type = "post" } = params;
  if (!type) {
    message.error("type is null");
    return;
  }
  const query = {
    ...params,
    searchText: params.text,
  };
  MyAxios.post("/search/all", query).then((res: any) => {
    if (type === "Article") {
      postList.value = res.dataList;
    } else if (type === "User") {
      userList.value = res.dataList;
    } else if (type === "Picture") {
      pictureList.value = res.dataList;
    }
  });
};
watchEffect(() => {
  searchParams.value = {
    ...searchParams,
    text: route.query.text,
    type: route.params.category,
  };
  loadData(searchParams.value);
});

const onTabChange = (key: string) => {
  router.push({
    path: `/${key}`,
    query: {
      text: searchParams.value.text,
    },
  });
};
</script>
