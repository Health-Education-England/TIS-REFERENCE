import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {DBC} from "./dbc.model";
import {DBCPopupService} from "./dbc-popup.service";
import {DBCService} from "./dbc.service";

@Component({
	selector: 'jhi-dbc-delete-dialog',
	templateUrl: './dbc-delete-dialog.component.html'
})
export class DBCDeleteDialogComponent {

	dBC: DBC;

	constructor(private jhiLanguageService: JhiLanguageService,
				private dBCService: DBCService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['dBC']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.dBCService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'dBCListModification',
				content: 'Deleted an dBC'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-dbc-delete-popup',
	template: ''
})
export class DBCDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private dBCPopupService: DBCPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.dBCPopupService
				.open(DBCDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
