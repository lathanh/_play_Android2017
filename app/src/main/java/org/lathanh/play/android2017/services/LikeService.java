package org.lathanh.play.android2017.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by rlathanh on 2017-10-04.
 */

public class LikeService {

  private static class Like {
    long userId;
    long thingId;

    @Override
    public int hashCode() {
      return new Long(userId).hashCode() ^ new Long(thingId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Like)) return false;
      Like otherLike = (Like) obj;
      return userId == otherLike.userId && thingId == otherLike.thingId;
    }
  }

  //== Operating fields =======================================================

  private final Set<Like> allLikes = new HashSet<>();


  public List<Long> getLikesByUser(long userId) {
    List<Long> likes = new ArrayList<>();
    for (Like like : allLikes) {
      if (like.userId == userId) likes.add(like.thingId);
    }
    return likes;
  }

  public List<Long> getLikersOfThing(long thingId) {
    List<Long> likes = new ArrayList<>();
    for (Like like : allLikes) {
      if (like.thingId == thingId) likes.add(like.userId);
    }
    return likes;
  }

  public int getNumLikersOfThing(long thingId) {
    return getLikersOfThing(thingId).size();
  }

}
