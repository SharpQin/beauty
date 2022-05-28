import { IPermission } from '@/shared/model/auth/permission.model';

export interface IRole {
  id?: number;
  name?: string;
  dsc?: string | null;
  permissions?: IPermission[];
}

export class Role implements IRole {
  constructor(public id?: number, public name?: string, public dsc?: string | null, public permissions?: IPermission[]) {}
}
