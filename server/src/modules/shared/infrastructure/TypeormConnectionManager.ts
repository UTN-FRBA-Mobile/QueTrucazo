import { DataSource, EntityManager, EntityTarget, ObjectLiteral, Repository } from 'typeorm';
import 'reflect-metadata'; // needed for typeorm
import { TypeormUser } from '../../users/infrastructure/TypeormUser';


export class TypeormConnectionManager {
  private static dataSource: DataSource;

  public static async start() {
    await this.createConnection();
  }

  public static getConnection<T extends ObjectLiteral>(entity: EntityTarget<T>): Repository<T> {
    if (this.dataSource === undefined) {
      throw new Error('Typeorm has not been initialized!');
    }
    return this.dataSource.getRepository(entity);
  }

  public static getManager(): EntityManager {
    return this.dataSource.manager;
  }

  private static async createConnection(): Promise<void> {
    const {
      DB_HOST = 'localhost',
      DB_PORT = '5432',
      DB_USER = 'postgres',
      DB_PASSWORD = 'password',
      DB_NAME = 'postgres',
      DB_SSL = 'false',
      DB_LOGGING = 'false'
    } = process.env;

    this.dataSource = new DataSource({
      type: 'postgres',
      host: DB_HOST,
      port: Number(DB_PORT),
      username: DB_USER,
      password: DB_PASSWORD,
      database: DB_NAME,
      ssl: DB_SSL === 'true'
        ? {
          rejectUnauthorized: false,
        }
        : undefined,
      entities: [
        TypeormUser,
      ],
      synchronize: true,
      logging: DB_LOGGING === 'true',
    });

    await this.dataSource
      .initialize()
      .then(() => {
        // eslint-disable-next-line no-console
        console.log('Postgres has been initialized!');
      })
      .catch((err) => {
        // eslint-disable-next-line no-console
        console.error('Error during Postgres initialization', err);
      });
  }
}
