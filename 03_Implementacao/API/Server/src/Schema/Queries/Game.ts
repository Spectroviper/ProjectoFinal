import { GraphQLList, GraphQLString } from 'graphql'
import { Games } from '../../Entities/Games';
import { AchievementType } from '../TypeDef/Achievement';
import { GameType } from '../TypeDef/Game';

export const GET_ALL_GAMES = {
    type: new GraphQLList(GameType),
    resolve() {
        return Games.find();
    }
}

export const GET_GAME = {
    type: GameType,
    args: {
        GameName: { type: GraphQLString },
    },
    async resolve(parent: any, args: any) {
        const {GameName} = args
        const game = await Games.findOne({where: {GameName: GameName}, relations: ['Achievements']})
        const gameGameName = game?.GameName;
        
        if (GameName === gameGameName) {
            return game;
        }
        else{
            throw new Error("GAME DOES NOT HAVE THE RIGHT GAME_NAME!");
        }
    }
}

export const GET_GAMES_ACHIEVEMENTS = {
    type: new GraphQLList(AchievementType),
    args: {
        GameName: { type: GraphQLString },
    },
    async resolve(parent: any, args: any) {
        const {GameName} = args
        const game = await Games.findOne({where: {GameName: GameName}, relations: ['Achievements']})
        const gameGameName = game?.GameName;

        if (GameName === gameGameName) {
            const gameAchievements = game?.Achievements || [];
            if(gameAchievements) {
                return gameAchievements
            }
            else{
                throw new Error("GAME DOES NOT HAVE ANY ACHIEVEMENTS");
            }
            
        }
        else{
            throw new Error("GAME DOES NOT HAVE THE RIGHT GAME_NAME!");
        }
    }
}

export const GET_GAMES_BY_CONSOLE = {
    type: new GraphQLList(GameType),
    args: {
        Console: { type: GraphQLString},
    },
    async resolve(parent: any, args: any) {
        const {Console} = args;
        const games = await Games.find({where: {Console: Console}});
        if(games) {
            return games;
        }
        else{
            throw new Error("THERE ARE NO GAMES WITH SUCH CONSOLE!");
        }
    }
};

export const GET_GAMES_BY_PUBLISHER = {
    type: new GraphQLList(GameType),
    args: {
        Publisher: { type: GraphQLString},
    },
    async resolve(parent: any, args: any) {
        const {Publisher} = args;
        const games = await Games.find({where: {Publisher: Publisher}});
        if(games) {
            return games;
        }
        else{
            throw new Error("THERE ARE NO GAMES WITH SUCH PUBLISHER!");
        }
    }
};

export const GET_GAMES_BY_DEVELOPER = {
    type: new GraphQLList(GameType),
    args: {
        Developer: { type: GraphQLString},
    },
    async resolve(parent: any, args: any) {
        const {Developer} = args;
        const games = await Games.find({where: {Developer: Developer}});
        if(games) {
            return games;
        }
        else{
            throw new Error("THERE ARE NO GAMES WITH SUCH DEVELOPER!");
        }
    }
};

export const GET_GAMES_BY_GENRE = {
    type: new GraphQLList(GameType),
    args: {
        Genre: { type: GraphQLString},
    },
    async resolve(parent: any, args: any) {
        const {Genre} = args;
        const games = await Games.find({where: {Genre: Genre}});
        if(games) {
            return games;
        }
        else{
            throw new Error("THERE ARE NO GAMES WITH SUCH GENRE!");
        }
    }
};
