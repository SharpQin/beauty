import { IProduct } from '@/shared/model/product/product.model';

export interface IProductItem {
  id?: number;
  name?: string | null;
  amount?: number | null;
  image?: string | null;
  dsc?: string | null;
  product?: IProduct | null;
}

export class ProductItem implements IProductItem {
  constructor(
    public id?: number,
    public name?: string | null,
    public amount?: number | null,
    public image?: string | null,
    public dsc?: string | null,
    public product?: IProduct | null
  ) {}
}
