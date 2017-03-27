/*
 * Copyright (c) 2016 Couchbase, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gittalent.model;

/**
 * Created by nraboy on 10/06/16.
 */
public class SocialMedia {

    private String twitter;

    //private String facebook;
//
// Add new requirements from Ravi below...
//

    ////
////
    public SocialMedia() {
    }

    public SocialMedia(String twitter) {
        this.twitter = twitter;
        //this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter.trim();
    }

    /*public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook.trim();
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SocialMedia that = (SocialMedia) o;

        if (twitter != null ? !twitter.equals(that.twitter) : that.twitter != null) return false;
        //if (facebook != null ? !facebook.equals(that.facebook) : that.facebook != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = twitter != null ? twitter.hashCode() : 0;
        //result = 31 * result + (facebook != null ? facebook.hashCode() : 0);
        return result;
    }
}
