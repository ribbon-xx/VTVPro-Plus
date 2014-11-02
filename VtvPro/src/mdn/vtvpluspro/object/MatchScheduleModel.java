package mdn.vtvpluspro.object;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by RibboN on 11/2/14.
 */
public class MatchScheduleModel implements Serializable {


    private long mID;
    private long mGUID;
    private long mStat_sport_id;
    private long mStat_competition_id;
    private TeamObject mHomeTeam;
    private TeamObject mAwayTeam;
    private long mSeason;
    /**
     * This date format is yyyy-MM-dd
     */
    private Date mDate;
    private Date mDatetime_of_play;
    private String mTimeZone;
    private String mAvailability;
    private String mAdditional_info;
    private String mNeutral_or_not;
    private String mScores_and_stats;
    private String mAttendance;
    private String mStatus;
    private long mHome_season_match_day;
    private long mAway_season_match_day;

    public MatchScheduleModel() {
    }

    public long getID() {
        return mID;
    }

    public void setID(long mID) {
        this.mID = mID;
    }

    public long getGUID() {
        return mGUID;
    }

    public void setGUID(long mGUID) {
        this.mGUID = mGUID;
    }

    public long getStat_sport_id() {
        return mStat_sport_id;
    }

    public void setStat_sport_id(long mStat_sport_id) {
        this.mStat_sport_id = mStat_sport_id;
    }

    public long getStat_competition_id() {
        return mStat_competition_id;
    }

    public void setStat_competition_id(long mStat_competition_id) {
        this.mStat_competition_id = mStat_competition_id;
    }

    public TeamObject getHomeTeam() {
        return mHomeTeam;
    }

    public void setHomeTeam(TeamObject mHomeTeam) {
        this.mHomeTeam = mHomeTeam;
    }

    public TeamObject getAwayTeam() {
        return mAwayTeam;
    }

    public void setAwayTeam(TeamObject mAwayTeam) {
        this.mAwayTeam = mAwayTeam;
    }

    public long getSeason() {
        return mSeason;
    }

    public void setSeason(long mSeason) {
        this.mSeason = mSeason;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public Date getDatetime_of_play() {
        return mDatetime_of_play;
    }

    public void setDatetime_of_play(Date mDatetime_of_play) {
        this.mDatetime_of_play = mDatetime_of_play;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String mTimeZone) {
        this.mTimeZone = mTimeZone;
    }

    public String getAvailability() {
        return mAvailability;
    }

    public void setAvailability(String mAvailability) {
        this.mAvailability = mAvailability;
    }

    public String getAdditional_info() {
        return mAdditional_info;
    }

    public void setAdditional_info(String mAdditional_info) {
        this.mAdditional_info = mAdditional_info;
    }

    public String getNeutral_or_not() {
        return mNeutral_or_not;
    }

    public void setNeutral_or_not(String mNeutral_or_not) {
        this.mNeutral_or_not = mNeutral_or_not;
    }

    public String getScores_and_stats() {
        return mScores_and_stats;
    }

    public void setScores_and_stats(String mScores_and_stats) {
        this.mScores_and_stats = mScores_and_stats;
    }

    public String getAttendance() {
        return mAttendance;
    }

    public void setAttendance(String mAttendance) {
        this.mAttendance = mAttendance;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public long getHome_season_match_day() {
        return mHome_season_match_day;
    }

    public void setHome_season_match_day(long mHome_season_match_day) {
        this.mHome_season_match_day = mHome_season_match_day;
    }

    public long getAway_season_match_day() {
        return mAway_season_match_day;
    }

    public void setAway_season_match_day(long mAway_season_match_day) {
        this.mAway_season_match_day = mAway_season_match_day;
    }
}
