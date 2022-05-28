import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import RoleService from '@/entities/auth/role/role.service';
import { IRole } from '@/shared/model/auth/role.model';

import { IPermission, Permission } from '@/shared/model/auth/permission.model';
import PermissionService from './permission.service';

const validations: any = {
  permission: {
    authKey: {
      required,
      maxLength: maxLength(30),
    },
  },
};

@Component({
  validations,
})
export default class PermissionUpdate extends Vue {
  @Inject('permissionService') private permissionService: () => PermissionService;
  @Inject('alertService') private alertService: () => AlertService;

  public permission: IPermission = new Permission();

  @Inject('roleService') private roleService: () => RoleService;

  public roles: IRole[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.permissionId) {
        vm.retrievePermission(to.params.permissionId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.permission.id) {
      this.permissionService()
        .update(this.permission)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('authApp.authPermission.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.permissionService()
        .create(this.permission)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('authApp.authPermission.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrievePermission(permissionId): void {
    this.permissionService()
      .find(permissionId)
      .then(res => {
        this.permission = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.roleService()
      .retrieve()
      .then(res => {
        this.roles = res.data;
      });
  }
}
