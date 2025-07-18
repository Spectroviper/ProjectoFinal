"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.schema = void 0;
const graphql_1 = require("graphql");
const User_1 = require("./Queries/User");
const Game_1 = require("./Queries/Game");
const Achievement_1 = require("./Queries/Achievement");
const Achievement_2 = require("./Mutations/Achievement");
const Game_2 = require("./Mutations/Game");
const Users_1 = require("./Mutations/Users");
const ChallengeInvite_1 = require("./Queries/ChallengeInvite");
const ChallengeInvite_2 = require("./Mutations/ChallengeInvite");
const Challenge_1 = require("./Mutations/Challenge");
const Challenge_2 = require("./Queries/Challenge");
const Friends_1 = require("./Mutations/Friends");
const Friends_2 = require("./Queries/Friends");
const ChallengeAchievement_1 = require("./Mutations/ChallengeAchievement");
const UserChallengeAchievement_1 = require("./Mutations/UserChallengeAchievement");
const UserAchievement_1 = require("./Queries/UserAchievement");
const UserChallengeAchievement_2 = require("./Queries/UserChallengeAchievement");
const ChallengeAchievement_2 = require("./Queries/ChallengeAchievement");
const RootQuery = new graphql_1.GraphQLObjectType({
    name: "RootQuery",
    fields: {
        //USER
        getAllUsers: User_1.GET_ALL_USERS,
        getUserByUserId: User_1.GET_USER_BY_USERID,
        getUserUserGames: User_1.GET_USER_USER_GAMES,
        getUserUserChallengeAchievements: User_1.GET_USER_USER_CHALLENGE_ACHIEVEMENTS,
        getUserUserAchievements: User_1.GET_USER_USER_ACHIEVEMENTS,
        getAllUsersByTotalPoints: User_1.GET_ALL_USERS_BY_TOTALPOINTS,
        getAllUserFriends: User_1.GET_ALL_USER_FRIENDS,
        getAllUsersInChallenge: User_1.GET_ALL_USERS_IN_CHALLENGE,
        getAllUsersNotInChallenge: User_1.GET_ALL_USERS_NOT_IN_CHALLENGE,
        searchUsers: User_1.SEARCH_USERS,
        searchUserGames: User_1.SEARCH_USER_GAMES,
        //GAME
        getAllGames: Game_1.GET_ALL_GAMES,
        getGameByGameId: Game_1.GET_GAME_BY_GAMEID,
        searchGames: Game_1.SEARCH_GAMES,
        //ACHIEVEMENT
        getAllAchievements: Achievement_1.GET_ALL_ACHIEVEMENTS,
        getAchievementByAchievementId: Achievement_1.GET_ACHIEVEMENT_BY_ACHIEVEMENTID,
        getAllAchievementsNotInAChallenge: Achievement_1.GET_ALL_ACHIEVEMENTS_NOT_IN_A_CHALLENGE,
        searchAchievements: Achievement_1.SEARCH_ACHIEVEMENTS,
        //USER ACHIEVEMENT
        getUserGameByUserGameId: UserAchievement_1.GET_USER_GAME_BY_USER_GAME_ID,
        getUserAchievementByUserAchievementId: UserAchievement_1.GET_USER_ACHIEVEMENT_BY_USER_ACHIEVEMENT_ID,
        //CHALLENGE INVITE
        getAllUserChallengeInvites: ChallengeInvite_1.GET_ALL_USER_CHALLENGE_INVITES,
        getAllChallengeInvitesByChallengeId: ChallengeInvite_1.GET_ALL_CHALLENGE_INVITES_BY_CHALLENGEID,
        //CHALLENGE
        getAllJoinableChallenges: Challenge_2.GET_ALL_JOINABLE_CHALLENGES,
        getChallengeByChallengeId: Challenge_2.GET_CHALLENGE_BY_CHALLENGEID,
        searchChallenges: Challenge_2.SEARCH_CHALLENGES,
        //CHALLENGE ACHIEVEMENTS
        getUserChallengeByUserChallengeId: UserChallengeAchievement_2.GET_USER_CHALLENGE_BY_USER_CHALLENGE_ID,
        getUserChallengeAchievementByUserChallengeAchievementId: UserChallengeAchievement_2.GET_USER_CHALLENGE_ACHIEVEMENT_BY_USER_CHALLENGE_ACHIEVEMENT_ID,
        getChallengeAchievementByChallengeAchievementId: ChallengeAchievement_2.GET_CHALLENGE_ACHIEVEMENT_BY_CHALLENGE_ACHIEVEMENT_ID,
        //FRIENDS
        getAllFriendInvitesByReceiverId: Friends_2.GET_ALL_FRIEND_INVITES_BY_RECIEVERID,
        getAllFriendInvitesBySenderId: Friends_2.GET_ALL_FRIEND_INVITES_BY_SENDERID,
        getAllFriendInvites: Friends_2.GET_ALL_FRIEND_INVITES,
        searchFriends: Friends_2.SEARCH_FRIENDS
    }
});
const Mutation = new graphql_1.GraphQLObjectType({
    name: "Mutation",
    fields: {
        //USER
        createUser: Users_1.CREATE_USER,
        deleteUser: Users_1.DELETE_USER,
        updateUser: Users_1.UPDATE_USER,
        logIn: Users_1.LOG_IN,
        //GAME
        createGame: Game_2.CREATE_GAME,
        deleteGame: Game_2.DELETE_GAME,
        updateGame: Game_2.UPDATE_GAME,
        addGame: Game_2.ADD_GAME,
        removeGame: Game_2.REMOVE_GAME,
        //ACHIEVEMENT
        createAchievement: Achievement_2.CREATE_ACHIEVEMENT,
        deleteAchievement: Achievement_2.DELETE_ACHIEVEMENT,
        updateAchievement: Achievement_2.UPDATE_ACHIEVEMENT,
        lockUnlockAchievement: Achievement_2.LOCK_UNLOCK_ACHIEVEMENT,
        //CHALLENGEACHIEVEMENT
        createChallengeAchievement: ChallengeAchievement_1.CREATE_CHALLENGE_ACHIEVEMENT,
        deleteChallengeAchievement: ChallengeAchievement_1.DELETE_CHALLENGE_ACHIEVEMENT,
        lockUnlockChallengeAchievement: UserChallengeAchievement_1.LOCK_UNLOCK_CHALLENGE_ACHIEVEMENT,
        //CHALLENGEINVITE
        inviteUserToChallenge: ChallengeInvite_2.INVITE_USER_TO_CHALLENGE,
        acceptChallengeInvite: ChallengeInvite_2.ACCEPT_CHALLENGE_INVITE,
        rejectChallengeInvite: ChallengeInvite_2.REJECT_CHALLENGE_INVITE,
        deleteChallengeInvite: ChallengeInvite_2.DELETE_CHALLENGE_INVITE,
        //CHALLENGE
        createChallenge: Challenge_1.CREATE_CHALLENGE,
        updateChallenge: Challenge_1.UPDATE_CHALLENGE,
        deleteChallenge: Challenge_1.DELETE_CHALLENGE,
        startChallenge: Challenge_1.START_CHALLENGE,
        endChallenge: Challenge_1.END_CHALLENGE,
        //FRIENDS
        inviteFriend: Friends_1.INVITE_FRIEND,
        acceptFriendInvite: Friends_1.ACCEPT_FRIEND_INVITE,
        rejectFriendInvite: Friends_1.REJECT_FRIEND_INVITE,
        deleteFriendInvite: Friends_1.DELETE_FRIEND_INVITE,
    }
});
exports.schema = new graphql_1.GraphQLSchema({
    query: RootQuery,
    mutation: Mutation
});
