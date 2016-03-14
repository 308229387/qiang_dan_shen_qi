package com.huangyezhaobiao.eventbus;

/**
 * Created by shenzhixin on 2015/12/1.
 */
public enum  EventType {
    //注册成功
     REGISTER_SUCCESS ,
    //注册失败
    REGISTER_FAILURE ,
    //别名设置成功
    ALIAS_SET_SUCCESS,
    //别名设置失败
    ALIAS_SET_FAILURE,
    //寻找所有图片
    EVENT_GALLERY_ALL_PHOTOS,
    //扫描带图片的文件夹
    EVENT_GALLERY_DIRS,
    //头像
    EVENT_SELECTED_AVATAR,
    //点击了item的删除按钮
    EVENT_GRIDVIEW_ITEM_CLOSE,
    //编辑模式下点击了item
    EVENT_GRIDVIEW_ITEM,
    //非编辑模式下点击了item
    EVENT_GRIDVIEW_ITEM_VIEW,
    //点击了加号按钮
    EVENT_GRIDVIEW_ITEM_ADD,
    //订单详情页---不是退单时间
    EVENT_TUIDAN_NOT_TIME,
    //订单详情页---第一次提交
    EVENT__FIRST_SUBMIT_TUIDAN,
    //补交退单
    EVENT_ADD_TUIDAN,
    //退单结果
    EVENT_TUIDAN_RESULT,
    //从浏览单张图片回来
    EVENT_BACK_FROM_VIEW_SINGLE,

    //从列表页打电话
    EVENT_TELEPHONE_FROM_LIST,
    //从详情页打电话
    EVENT_TELEPHONE_FROM_DETAIL
}
