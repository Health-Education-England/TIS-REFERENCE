import {Routes} from "@angular/router";
import {UserRouteAccessService, Principal} from "../../shared";
import {GradeComponent} from "./grade.component";
import {GradeDetailComponent} from "./grade-detail.component";
import {GradePopupComponent} from "./grade-dialog.component";
import {GradeDeletePopupComponent} from "./grade-delete-dialog.component";


export const gradeRoute: Routes = [
	{
		path: 'grade',
		component: GradeComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.grade.home.title'
		}
	}, {
		path: 'grade/:id',
		component: GradeDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.grade.home.title'
		}
	}
];

export const gradePopupRoute: Routes = [
	{
		path: 'grade-new',
		component: GradePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.grade.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'grade/:id/edit',
		component: GradePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.grade.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'grade/:id/delete',
		component: GradeDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'referenceApp.grade.home.title'
		},
		outlet: 'popup'
	}
];
