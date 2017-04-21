import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {PaginationUtil} from "ng-jhipster";
import {MedicalSchoolComponent} from "./medical-school.component";
import {MedicalSchoolDetailComponent} from "./medical-school-detail.component";
import {MedicalSchoolPopupComponent} from "./medical-school-dialog.component";
import {MedicalSchoolDeletePopupComponent} from "./medical-school-delete-dialog.component";

@Injectable()
export class MedicalSchoolResolvePagingParams implements Resolve<any> {

	constructor(private paginationUtil: PaginationUtil) {
	}

	resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		let page = route.queryParams['page'] ? route.queryParams['page'] : '1';
		let sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
		return {
			page: this.paginationUtil.parsePage(page),
			predicate: this.paginationUtil.parsePredicate(sort),
			ascending: this.paginationUtil.parseAscending(sort)
		};
	}
}

export const medicalSchoolRoute: Routes = [
	{
		path: 'medical-school',
		component: MedicalSchoolComponent,
		resolve: {
			'pagingParams': MedicalSchoolResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.medicalSchool.home.title'
		}
	}, {
		path: 'medical-school/:id',
		component: MedicalSchoolDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.medicalSchool.home.title'
		}
	}
];

export const medicalSchoolPopupRoute: Routes = [
	{
		path: 'medical-school-new',
		component: MedicalSchoolPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.medicalSchool.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'medical-school/:id/edit',
		component: MedicalSchoolPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.medicalSchool.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'medical-school/:id/delete',
		component: MedicalSchoolDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.medicalSchool.home.title'
		},
		outlet: 'popup'
	}
];
