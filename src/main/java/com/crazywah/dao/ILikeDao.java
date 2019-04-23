package com.crazywah.dao;

import com.crazywah.entity.Like;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ILikeDao {

    @Select("select * from t_liken where fromId = #{fromId} AND objId = #{objId} AND objType = #{objType}")
    Like getLikeByIdAndType(Like like);

    @Insert("insert into t_like (fromId, objId, objType, likeTime) values (#{fromId}, #{objId}, #{objType} ,#{likeTime})")
    void addLike(Like like);

    @Insert("update t_like set likeTime = #{likeTime} where likeId = #{likeId}")
    void updateLike(Like like);

    @Delete("delete * from t_like where likeId = #{likeId}")
    void deleteLike(Like like);

}
