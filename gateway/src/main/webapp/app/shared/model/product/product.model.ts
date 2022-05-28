import { IProductItem } from '@/shared/model/product/product-item.model';
import { IProductCategory } from '@/shared/model/product/product-category.model';

import { ProductType } from '@/shared/model/enumerations/product-type.model';
import { ProductStatus } from '@/shared/model/enumerations/product-status.model';
export interface IProduct {
  id?: number;
  name?: string;
  price?: number;
  type?: ProductType | null;
  image?: string | null;
  releaseDate?: Date | null;
  liveTime?: Date | null;
  createdTime?: Date | null;
  updatedTime?: Date | null;
  stock?: number | null;
  showed?: boolean | null;
  status?: ProductStatus | null;
  dsc?: string | null;
  items?: IProductItem[] | null;
  category?: IProductCategory | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string,
    public price?: number,
    public type?: ProductType | null,
    public image?: string | null,
    public releaseDate?: Date | null,
    public liveTime?: Date | null,
    public createdTime?: Date | null,
    public updatedTime?: Date | null,
    public stock?: number | null,
    public showed?: boolean | null,
    public status?: ProductStatus | null,
    public dsc?: string | null,
    public items?: IProductItem[] | null,
    public category?: IProductCategory | null
  ) {
    this.showed = this.showed ?? false;
  }
}
