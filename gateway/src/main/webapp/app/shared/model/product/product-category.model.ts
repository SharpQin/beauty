import { IProduct } from '@/shared/model/product/product.model';

export interface IProductCategory {
  id?: number;
  name?: string;
  dsc?: string | null;
  products?: IProduct[] | null;
}

export class ProductCategory implements IProductCategory {
  constructor(public id?: number, public name?: string, public dsc?: string | null, public products?: IProduct[] | null) {}
}
