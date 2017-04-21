import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {DBC} from "./dbc.model";
import {DBCPopupService} from "./dbc-popup.service";
import {DBCService} from "./dbc.service";
@Component({
	selector: 'jhi-dbc-dialog',
	templateUrl: './dbc-dialog.component.html'
})
export class DBCDialogComponent implements OnInit {

	dBC: DBC;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private dBCService: DBCService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['dBC']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.dBC.id !== undefined) {
			this.dBCService.update(this.dBC)
				.subscribe((res: DBC) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.dBCService.create(this.dBC)
				.subscribe((res: DBC) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: DBC) {
		this.eventManager.broadcast({name: 'dBCListModification', content: 'OK'});
		this.isSaving = false;
		this.activeModal.dismiss(result);
	}

	private onSaveError(error) {
		this.isSaving = false;
		this.onError(error);
	}

	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}

@Component({
	selector: 'jhi-dbc-popup',
	template: ''
})
export class DBCPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private dBCPopupService: DBCPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.dBCPopupService
					.open(DBCDialogComponent, params['id']);
			} else {
				this.modalRef = this.dBCPopupService
					.open(DBCDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
