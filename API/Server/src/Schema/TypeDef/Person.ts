import {GraphQLObjectType, GraphQLID, GraphQLString, GraphQLInt, GraphQLList} from 'graphql';
import { FindOptionsWhere } from 'typeorm';
import { PersonAchievements } from '../../Entities/PersonAchievements';
import { Persons } from '../../Entities/Persons';
import { CustomDate } from '../CustomTypes/Date';
import { CustomImage } from '../CustomTypes/Image';
import { GameType } from './Game';
import { PersonAchievementType } from './PersonAchievementType';

export const PersonType: GraphQLObjectType<Persons> = new GraphQLObjectType({
    name: "Person",
    fields: () => ({
        id: { type: GraphQLID },
        UserName: { type: GraphQLString },
        Biography: { type: GraphQLString },
        MemberSince: { type: CustomDate },
        LastLogin: { type: CustomDate },
        TotalPoints: { type: GraphQLInt },
        AverageCompletion: {  type: GraphQLInt },
        SiteRank: { type: GraphQLInt },
        Image: { type: CustomImage },
        Plays: { type: new GraphQLList(GameType) },
        PersonAchievements: { type: new GraphQLList(PersonAchievementType),
            resolve: async (parent: Persons) => {
                return parent.PersonAchievements || [];
            },
        },
    })
})