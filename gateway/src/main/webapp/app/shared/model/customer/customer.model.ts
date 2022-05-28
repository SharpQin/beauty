import { IAddress } from '@/shared/model/customer/address.model';
import { ICusAccount } from '@/shared/model/customer/cus-account.model';

export interface ICustomer {
  id?: number;
  name?: string;
  phone?: string | null;
  email?: string | null;
  birthday?: Date | null;
  imageUrl?: string | null;
  langKey?: string | null;
  vip?: number;
  level?: number;
  point?: number;
  activated?: boolean | null;
  createdDate?: Date | null;
  lastModifiedDate?: Date | null;
  addresses?: IAddress[] | null;
  accounts?: ICusAccount[] | null;
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public name?: string,
    public phone?: string | null,
    public email?: string | null,
    public birthday?: Date | null,
    public imageUrl?: string | null,
    public langKey?: string | null,
    public vip?: number,
    public level?: number,
    public point?: number,
    public activated?: boolean | null,
    public createdDate?: Date | null,
    public lastModifiedDate?: Date | null,
    public addresses?: IAddress[] | null,
    public accounts?: ICusAccount[] | null
  ) {
    this.activated = this.activated ?? false;
  }
}
