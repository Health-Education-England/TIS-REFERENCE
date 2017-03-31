import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {Settled} from "./settled.model";
import {SettledPopupService} from "./settled-popup.service";
import {SettledService} from "./settled.service";

@Component({
	selector: 'jhi-settled-delete-dialog',
	templateUrl: './settled-delete-dialog.component.html'
})
export class SettledDeleteDialogComponent {

	settled: Settled;

	constructor(private jhiLanguageService: JhiLanguageService,
				private settledService: SettledService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['settled']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.settledService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'settledListModification',
				content: 'Deleted an settled'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-settled-delete-popup',
	template: ''
})
export class SettledDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private settledPopupService: SettledPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.settledPopupService
				.open(SettledDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
