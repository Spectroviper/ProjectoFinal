import { BaseEntity, Column, Entity, OneToMany, PrimaryGeneratedColumn } from "typeorm";
import { Achievements } from "./Achievements";
import { UserGames } from "./UserGames";

@Entity()
export class Games extends BaseEntity {

    @PrimaryGeneratedColumn()
    GameId!: number;

    @Column()
    GameName!: string;

    @Column({nullable: true})
    About!: string;

    @Column()
    Console!: string;
    
    @Column()
    Developer!: string;

    @Column()
    Publisher!: string;

    @Column()
    Genre!: string;

    @Column()
    ReleaseDate!: Date;

    @Column({nullable: true})
    Image!: string;

    @Column({default: "system"})
    CreatedBy!: string;

    @OneToMany(() => Achievements, achievements => achievements.Game, {nullable: true})
    Achievements!: Achievements[];

    @OneToMany(() => UserGames, userGames => userGames.Game, {nullable: true})
    UserGames!: UserGames[];

}