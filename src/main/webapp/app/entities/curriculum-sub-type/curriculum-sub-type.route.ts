import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {CurriculumSubTypeComponent} from "./curriculum-sub-type.component";
import {CurriculumSubTypeDetailComponent} from "./curriculum-sub-type-detail.component";
import {CurriculumSubTypePopupComponent} from "./curriculum-sub-type-dialog.component";
import {CurriculumSubTypeDeletePopupComponent} from "./curriculum-sub-type-delete-dialog.component";


export const curriculumSubTypeRoute: Routes = [
	{
		path: 'curriculum-sub-type',
		component: CurriculumSubTypeComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.curriculumSubType.home.title'
		}
	}, {
		path: 'curriculum-sub-type/:id',
		component: CurriculumSubTypeDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.curriculumSubType.home.title'
		}
	}
];

export const curriculumSubTypePopupRoute: Routes = [
	{
		path: 'curriculum-sub-type-new',
		component: CurriculumSubTypePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.curriculumSubType.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'curriculum-sub-type/:id/edit',
		component: CurriculumSubTypePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.curriculumSubType.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'curriculum-sub-type/:id/delete',
		component: CurriculumSubTypeDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.curriculumSubType.home.title'
		},
		outlet: 'popup'
	}
];
