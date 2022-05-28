/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from '@/shared/date/filters';
import ProductService from '@/entities/product/product/product.service';
import { Product } from '@/shared/model/product/product.model';
import { ProductType } from '@/shared/model/enumerations/product-type.model';
import { ProductStatus } from '@/shared/model/enumerations/product-status.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Product Service', () => {
    let service: ProductService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new ProductService();
      currentDate = new Date();
      elemDefault = new Product(
        123,
        'AAAAAAA',
        0,
        ProductType.SUIT,
        'AAAAAAA',
        currentDate,
        currentDate,
        currentDate,
        currentDate,
        0,
        false,
        ProductStatus.AVAILABLE,
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            releaseDate: dayjs(currentDate).format(DATE_FORMAT),
            liveTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            createdTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            updatedTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Product', async () => {
        const returnedFromService = Object.assign(
          {
            id: 123,
            releaseDate: dayjs(currentDate).format(DATE_FORMAT),
            liveTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            createdTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            updatedTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            releaseDate: currentDate,
            liveTime: currentDate,
            createdTime: currentDate,
            updatedTime: currentDate,
          },
          returnedFromService
        );

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Product', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Product', async () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            price: 1,
            type: 'BBBBBB',
            image: 'BBBBBB',
            releaseDate: dayjs(currentDate).format(DATE_FORMAT),
            liveTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            createdTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            updatedTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            stock: 1,
            showed: true,
            status: 'BBBBBB',
            dsc: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            releaseDate: currentDate,
            liveTime: currentDate,
            createdTime: currentDate,
            updatedTime: currentDate,
          },
          returnedFromService
        );
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Product', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Product', async () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            type: 'BBBBBB',
            releaseDate: dayjs(currentDate).format(DATE_FORMAT),
            liveTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            createdTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            updatedTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            showed: true,
            dsc: 'BBBBBB',
          },
          new Product()
        );
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            releaseDate: currentDate,
            liveTime: currentDate,
            createdTime: currentDate,
            updatedTime: currentDate,
          },
          returnedFromService
        );
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Product', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Product', async () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            price: 1,
            type: 'BBBBBB',
            image: 'BBBBBB',
            releaseDate: dayjs(currentDate).format(DATE_FORMAT),
            liveTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            createdTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            updatedTime: dayjs(currentDate).format(DATE_TIME_FORMAT),
            stock: 1,
            showed: true,
            status: 'BBBBBB',
            dsc: 'BBBBBB',
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            releaseDate: currentDate,
            liveTime: currentDate,
            createdTime: currentDate,
            updatedTime: currentDate,
          },
          returnedFromService
        );
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Product', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Product', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Product', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
