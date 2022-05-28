import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import PermissionService from '@/entities/auth/permission/permission.service';
import { IPermission } from '@/shared/model/auth/permission.model';

import { IRole, Role } from '@/shared/model/auth/role.model';
import RoleService from './role.service';

const validations: any = {
  role: {
    name: {
      required,
      maxLength: maxLength(30),
    },
    dsc: {},
    permissions: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class RoleUpdate extends Vue {
  @Inject('roleService') private roleService: () => RoleService;
  @Inject('alertService') private alertService: () => AlertService;

  public role: IRole = new Role();

  @Inject('permissionService') private permissionService: () => PermissionService;

  public permissions: IPermission[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.roleId) {
        vm.retrieveRole(to.params.roleId);
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
    this.role.permissions = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.role.id) {
      this.roleService()
        .update(this.role)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('authApp.authRole.updated', { param: param.id });
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
      this.roleService()
        .create(this.role)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('authApp.authRole.created', { param: param.id });
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

  public retrieveRole(roleId): void {
    this.roleService()
      .find(roleId)
      .then(res => {
        this.role = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.permissionService()
      .retrieve()
      .then(res => {
        this.permissions = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}
