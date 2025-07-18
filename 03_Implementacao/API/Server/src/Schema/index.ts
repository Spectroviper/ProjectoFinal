import { GraphQLObjectType, GraphQLSchema } from "graphql";
import { GET_ALL_USER_FRIENDS, GET_ALL_USERS, GET_ALL_USERS_BY_TOTALPOINTS, GET_ALL_USERS_NOT_IN_CHALLENGE, GET_USER_BY_USERID, GET_USER_USER_ACHIEVEMENTS, GET_USER_USER_GAMES, SEARCH_USER_GAMES, SEARCH_USERS} from './Queries/User'
import { GET_ALL_GAMES, GET_GAME_BY_GAMEID, SEARCH_GAMES } from "./Queries/Game";
import { GET_ACHIEVEMENT_BY_ACHIEVEMENTID,  GET_ALL_ACHIEVEMENTS_NOT_IN_A_CHALLENGE, SEARCH_ACHIEVEMENTS } from "./Queries/Achievement";
import { CREATE_ACHIEVEMENT, DELETE_ACHIEVEMENT, UPDATE_ACHIEVEMENT, LOCK_UNLOCK_ACHIEVEMENT } from "./Mutations/Achievement";
import { CREATE_GAME, DELETE_GAME, UPDATE_GAME, ADD_GAME, REMOVE_GAME } from "./Mutations/Game";
import { CREATE_USER, DELETE_USER, LOG_IN, UPDATE_USER } from "./Mutations/Users";
import { GET_ALL_CHALLENGE_INVITES_BY_CHALLENGEID, GET_ALL_USER_CHALLENGE_INVITES } from "./Queries/ChallengeInvite";
import { ACCEPT_CHALLENGE_INVITE, DELETE_CHALLENGE_INVITE, INVITE_USER_TO_CHALLENGE, REJECT_CHALLENGE_INVITE } from "./Mutations/ChallengeInvite";
import { CREATE_CHALLENGE, DELETE_CHALLENGE, END_CHALLENGE, START_CHALLENGE, UPDATE_CHALLENGE } from "./Mutations/Challenge";
import { GET_ALL_JOINABLE_CHALLENGES, GET_CHALLENGE_BY_CHALLENGEID, SEARCH_CHALLENGES } from "./Queries/Challenge";
import { ACCEPT_FRIEND_INVITE, DELETE_FRIEND_INVITE, INVITE_FRIEND, REJECT_FRIEND_INVITE } from "./Mutations/Friends";
import { GET_ALL_FRIEND_INVITES,  SEARCH_FRIENDS } from "./Queries/Friends";
import { CREATE_CHALLENGE_ACHIEVEMENT, DELETE_CHALLENGE_ACHIEVEMENT } from "./Mutations/ChallengeAchievement";
import { LOCK_UNLOCK_CHALLENGE_ACHIEVEMENT } from "./Mutations/UserChallengeAchievement";
import { GET_USER_ACHIEVEMENT_BY_USER_ACHIEVEMENT_ID, GET_USER_GAME_BY_USER_GAME_ID } from "./Queries/UserAchievement";
import { GET_USER_CHALLENGE_ACHIEVEMENT_BY_USER_CHALLENGE_ACHIEVEMENT_ID, GET_USER_CHALLENGE_BY_USER_CHALLENGE_ID } from "./Queries/UserChallengeAchievement";
import { GET_CHALLENGE_ACHIEVEMENT_BY_CHALLENGE_ACHIEVEMENT_ID } from "./Queries/ChallengeAchievement";


const RootQuery = new GraphQLObjectType({
    name: "RootQuery",
    fields: {
        //USER
        getAllUsers: GET_ALL_USERS,
        getUserByUserId: GET_USER_BY_USERID,
        getUserUserGames: GET_USER_USER_GAMES,
        getUserUserAchievements: GET_USER_USER_ACHIEVEMENTS,
        getAllUsersByTotalPoints: GET_ALL_USERS_BY_TOTALPOINTS,
        getAllUserFriends: GET_ALL_USER_FRIENDS,
        getAllUsersNotInChallenge: GET_ALL_USERS_NOT_IN_CHALLENGE,
        searchUsers: SEARCH_USERS,
        searchUserGames: SEARCH_USER_GAMES,
        //GAME
        getAllGames: GET_ALL_GAMES,
        getGameByGameId: GET_GAME_BY_GAMEID,
        searchGames: SEARCH_GAMES,
        //ACHIEVEMENT
        getAchievementByAchievementId: GET_ACHIEVEMENT_BY_ACHIEVEMENTID,
        getAllAchievementsNotInAChallenge: GET_ALL_ACHIEVEMENTS_NOT_IN_A_CHALLENGE,
        searchAchievements: SEARCH_ACHIEVEMENTS,
        //USER ACHIEVEMENT
        getUserGameByUserGameId: GET_USER_GAME_BY_USER_GAME_ID,
        getUserAchievementByUserAchievementId: GET_USER_ACHIEVEMENT_BY_USER_ACHIEVEMENT_ID,
        //CHALLENGE INVITE
        getAllUserChallengeInvites: GET_ALL_USER_CHALLENGE_INVITES,
        getAllChallengeInvitesByChallengeId: GET_ALL_CHALLENGE_INVITES_BY_CHALLENGEID,
        //CHALLENGE
        getAllJoinableChallenges: GET_ALL_JOINABLE_CHALLENGES,
        getChallengeByChallengeId: GET_CHALLENGE_BY_CHALLENGEID,
        searchChallenges: SEARCH_CHALLENGES,
        //CHALLENGE ACHIEVEMENTS
        getUserChallengeByUserChallengeId: GET_USER_CHALLENGE_BY_USER_CHALLENGE_ID,
        getUserChallengeAchievementByUserChallengeAchievementId: GET_USER_CHALLENGE_ACHIEVEMENT_BY_USER_CHALLENGE_ACHIEVEMENT_ID,
        getChallengeAchievementByChallengeAchievementId: GET_CHALLENGE_ACHIEVEMENT_BY_CHALLENGE_ACHIEVEMENT_ID,
        //FRIENDS
        getAllFriendInvites: GET_ALL_FRIEND_INVITES,
        searchFriends: SEARCH_FRIENDS
    }
})

const Mutation = new GraphQLObjectType({
    name: "Mutation",
    fields: {
        //USER
        createUser: CREATE_USER,
        deleteUser: DELETE_USER,
        updateUser: UPDATE_USER,
        logIn: LOG_IN,
        //GAME
        createGame: CREATE_GAME,
        deleteGame: DELETE_GAME,
        updateGame: UPDATE_GAME,
        addGame: ADD_GAME,
        removeGame: REMOVE_GAME,
        //ACHIEVEMENT
        createAchievement: CREATE_ACHIEVEMENT,
        deleteAchievement: DELETE_ACHIEVEMENT,
        updateAchievement: UPDATE_ACHIEVEMENT,
        lockUnlockAchievement: LOCK_UNLOCK_ACHIEVEMENT,
        //CHALLENGEACHIEVEMENT
        createChallengeAchievement: CREATE_CHALLENGE_ACHIEVEMENT,
        deleteChallengeAchievement: DELETE_CHALLENGE_ACHIEVEMENT,
        lockUnlockChallengeAchievement: LOCK_UNLOCK_CHALLENGE_ACHIEVEMENT,
        //CHALLENGEINVITE
        inviteUserToChallenge: INVITE_USER_TO_CHALLENGE,
        acceptChallengeInvite: ACCEPT_CHALLENGE_INVITE,
        rejectChallengeInvite: REJECT_CHALLENGE_INVITE,
        deleteChallengeInvite: DELETE_CHALLENGE_INVITE,
        //CHALLENGE
        createChallenge: CREATE_CHALLENGE,
        updateChallenge: UPDATE_CHALLENGE,
        deleteChallenge: DELETE_CHALLENGE,
        startChallenge: START_CHALLENGE,
        endChallenge: END_CHALLENGE,
        //FRIENDS
        inviteFriend: INVITE_FRIEND,
        acceptFriendInvite: ACCEPT_FRIEND_INVITE,
        rejectFriendInvite: REJECT_FRIEND_INVITE,
        deleteFriendInvite: DELETE_FRIEND_INVITE,    
    }
})


export const schema = new GraphQLSchema({
    query: RootQuery,
    mutation: Mutation
});